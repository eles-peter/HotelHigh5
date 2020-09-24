import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HotelService} from "../../services/hotel.service";
import {HotelListItemModel} from "../../models/hotelListItem.model";
import {PopupService} from "../../services/popup.service";
import {getPublicId} from "../../utils/cloudinaryPublicIdHandler";
import {LoginService} from "../../services/login.service";
import {AuthenticatedLoginDetailsModel} from "../../models/authenticatedLoginDetails.model";
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {HotelFeatureTypeOptionModel} from "../../models/hotelFeatureTypeOption.model";
import {HotelFormDataModel} from "../../models/hotelFormData.model";
import {dateToJsonDateString} from "../../utils/dateUtils";
import {HotelListItemSubListModel} from "../../models/hotelListItemSubList.model";
import {ViewportScroller} from "@angular/common";
import {LatLngBounds} from "@agm/core";

declare var google: any;

@Component({
  selector: 'app-hotel-list',
  templateUrl: './hotel-list.component.html',
  styleUrls: ['./hotel-list.component.css']
})
export class HotelListComponent implements OnInit, OnDestroy {

  hotelList: HotelListItemModel[] = [];
  account: AuthenticatedLoginDetailsModel;
  filterForm: FormGroup;
  filterData = null;
  hotelFeatureTypeOption: HotelFeatureTypeOptionModel[];
  listPageNumber: number = 0;
  pageNumbers: number[];
  mapCenter = {
    latitude: 47.497913,
    longitude: 19.040236,
  }
  mapBounds: LatLngBounds;
  hoveredHotelId = -1;
  dateFilter = (date: Date) =>
    new Date(date.setHours(0,0,0,0)).getTime() >= new Date().setHours(0,0,0,0);

  constructor(private hotelService: HotelService, private route: ActivatedRoute,
              private router: Router, private popupService: PopupService,
              private viewportScroller: ViewportScroller, private loginService: LoginService) {

    this.filterForm = new FormGroup({
      'numberOfGuests': new FormControl(null,
        [Validators.required,
          Validators.min(1)]),
      'bookingDateRange': new FormControl(null,
        Validators.required),
      'hotelFeatures': new FormArray([]),
    })
  }

  ngOnDestroy() {
    this.listPageNumber = 0;
  }

  ngOnInit(): void {

    this.account = this.loginService.authenticatedLoginDetailsModel.getValue();
    if (!this.account) {
      this.loginService.checkSession().subscribe(
        (response) => {
          if (response) {
            this.loginService.authenticatedLoginDetailsModel.next(response);
          }
          this.account = response;
          if (!this.account || this.account.role != "ROLE_HOTELOWNER") {
            this.listHotel();
          } else {
            this.router.navigate(['admin/hotel'])
          }
        }
      )
    } else {
      if (this.account.role != "ROLE_HOTELOWNER") {
        this.listHotel();
      } else {
        this.router.navigate(['admin/hotel'])
      }
    }
  }

  listHotel = () => {
    this.hotelService.getHotelFormData().subscribe(
      (hotelFormData: HotelFormDataModel) => {
        this.hotelFeatureTypeOption = hotelFormData.hotelFeatures;
        this.createHotelFeaturesCheckboxControl();
      }
    );

    if (this.router.url.startsWith('/hotel/filter?')) {
      this.route.queryParams.subscribe(
        queryParams => {
          this.filterData = {
            numberOfGuests: queryParams['numberOfGuests'],
            startDate: queryParams['startDate'],
            endDate: queryParams['endDate'],
            hotelFeatures: queryParams['hotelFeatures'],
            listPageNumber: queryParams['listPageNumber'],
          };
          if (this.filterData.hotelFeatures == undefined) {
            this.filterData.hotelFeatures = '';
          }
          if (this.filterData.listPageNumber == undefined) {
            this.listPageNumber = 0;
          } else {
            this.listPageNumber = this.filterData.listPageNumber;
          }

          this.filterForm.controls['numberOfGuests'].setValue(this.filterData.numberOfGuests);

          let filterBookingDateRange = {
            begin: new Date(this.filterData.startDate),
            end: new Date(this.filterData.endDate),
          };
          this.filterForm.controls['bookingDateRange'].setValue(filterBookingDateRange);


          this.hotelService.getFilteredHotelList(this.filterData, this.listPageNumber).subscribe(
            (response: HotelListItemSubListModel) => {
              this.hotelList = response.hotelSubList;
              this.createMapBounds();
              this.listPageNumber = response.listPageNumber;
              this.pageNumbers = this.generatePageNumberArray(response.fullNumberOfPages);
            }
          );
        }
      );
    } else {
      this.route.queryParams.subscribe(
        queryParams => {
          this.listPageNumber = queryParams['listPageNumber'];
          this.hotelService.listHotel(this.listPageNumber).subscribe(
            (response: HotelListItemSubListModel) => {
              this.hotelList = response.hotelSubList;
              this.createMapBounds();
              this.listPageNumber = response.listPageNumber;
              this.pageNumbers = this.generatePageNumberArray(response.fullNumberOfPages);
            }
          );
        }
      );
    }
  };

  createMapBounds() {
    this.mapBounds = new google.maps.LatLngBounds();
    this.hotelList.forEach(hotel => {
      this.mapBounds.extend(new google.maps.LatLng(hotel.latitude - 0.003, hotel.longitude - 0.0030))
      this.mapBounds.extend(new google.maps.LatLng(hotel.latitude + 0.003, hotel.longitude + 0.0030))
    });
  }

  onPageNumClick(pageNum: number) {
    if (this.router.url.startsWith('/hotel/filter?')) {
      this.route.queryParams.subscribe(
        queryParamsFromRoute => {
          const queryParams = {
            numberOfGuests: queryParamsFromRoute['numberOfGuests'],
            startDate: queryParamsFromRoute['startDate'],
            endDate: queryParamsFromRoute['endDate'],
            hotelFeatures: queryParamsFromRoute['hotelFeatures'],
            'listPageNumber': pageNum
          };
          this.router.navigate(['/hotel/filter'], {queryParams});
        }
      );
    } else {
      const queryParams = {'listPageNumber': pageNum};
      this.router.navigate(['hotel'], {queryParams});
    }
  }

  generatePageNumberArray(numOfHotels: number) {
    let numArray = new Array<number>();
    for (let i = 0; i < numOfHotels; i++) {
      numArray.push(i);
    }
    return numArray;
  }

  filterHotelList() {
    const queryParams = {
      'numberOfGuests': this.filterForm.value.numberOfGuests,
      'startDate': dateToJsonDateString(this.filterForm.value.bookingDateRange.begin),
      'endDate': dateToJsonDateString(this.filterForm.value.bookingDateRange.end),
      'hotelFeatures': this.createHotelFeaturesFilterArrayToSend().join(', '),
    };
    this.router.navigate(['/hotel/filter'], {queryParams})
  }

  resetFilters() {
    this.filterForm.reset();
    //TODO resetelni a naptárat!!!
    // this.flatpickrInstance.clear();
  }

  backToHotelList() {
    this.router.navigate(['/hotel'])
  }

  deleteHotel(id: number): void {
    this.popupService.openConfirmPopup("Biztos törölni szeretnéd a tételt?")
      .afterClosed().subscribe(res => {
      if (res) {
        this.hotelService.deleteHotel(id).subscribe(
          (response: HotelListItemModel[]) => {
            this.hotelList = response;
          },
          error => console.warn(error),
        );
      }
    })
  }


  updateHotel(id: number): void {
    this.router.navigate(['/admin/hotel-update'])
  }

  hotelDetail(id: number): void {
    if (this.filterData) {
      const queryParams = {
        'numberOfGuests': this.filterData.numberOfGuests,
        'startDate': this.filterData.startDate,
        'endDate': this.filterData.endDate,
      };
      this.router.navigate(['/hotel/', id, 'filter'], {queryParams});

    } else {
      this.router.navigate(['/hotel/', id]);
    }
  }

  getPublicId(imgURL: string) {
    return getPublicId(imgURL);
  }

  private createHotelFeaturesCheckboxControl() {
    this.hotelFeatureTypeOption.forEach(() => {
        const control = new FormControl(false);
        (this.filterForm.controls.hotelFeatures as FormArray).push(control);
      }
    );
  }

  private createHotelFeaturesFilterArrayToSend(): string[] {
    return this.filterForm.value.hotelFeatures
      .map((hotelFeatures, index) => hotelFeatures ? this.hotelFeatureTypeOption[index].name : null)
      .filter(hotelFeatures => hotelFeatures !== null);
  }

  // goToHotel(id: number) {
  //   const hotelDivElement = document.getElementById(String(id));
  //   hotelDivElement.scrollIntoView({behavior: "smooth", block: "start", inline: "nearest"});
  // }

  goToHotel(id: number) {
    this.viewportScroller.setOffset([0, 70]);
    this.viewportScroller.scrollToAnchor(String(id));

  }

  setHoveredHotelId(id: number) {
    this.hoveredHotelId = id;
  }

  unsetHoveredHotelId(id: number) {
    this.hoveredHotelId = -1;
  }
}

