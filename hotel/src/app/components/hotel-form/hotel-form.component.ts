import {Component, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {HotelService} from "../../services/hotel.service";
import {HotelFeatureTypeOptionModel} from "../../models/hotelFeatureTypeOption.model";
import {HotelTypeOptionModel} from "../../models/hotelTypeOption.model";
import {HotelFormDataModel} from "../../models/hotelFormData.model";
import {HotelCreateItemModel} from "../../models/hotelCreateItem.model";
import {LoginService} from "../../services/login.service";
import {validationHandler} from "../../utils/validationHandler";

@Component({
  selector: 'app-hotel-form',
  templateUrl: './hotel-form.component.html',
  styleUrls: ['./hotel-form.component.css']
})
export class HotelFormComponent implements OnInit {

  hotelForm: FormGroup;
  hotelFeatureTypeOption: HotelFeatureTypeOptionModel[];
  hotelTypeOption: HotelTypeOptionModel[];
  hotelIdFromLogin: number;
  isUpdate: boolean;

  constructor(private hotelService: HotelService, private loginService: LoginService, private route: ActivatedRoute, private router: Router) {
    this.hotelForm = new FormGroup({
      'name': new FormControl('', Validators.required),
      'postalCode': new FormControl('', Validators.required),
      'city': new FormControl('', Validators.required),
      'streetAddress': new FormControl('', Validators.required),
      'hotelType': new FormControl('', Validators.required),
      'description': new FormControl('',
        [Validators.required,
          Validators.minLength(500),
          Validators.maxLength(2000)]),
        'hotelFeatures': new FormArray([]),
        'file': new FormControl(null),
        'fileSource': new FormControl('')
      }
    );
  }

  ngOnInit() {
    let account = this.loginService.authenticatedLoginDetailsModel.getValue();
    if (account != null) {
      let role = account.role;
      if (role !== "ROLE_HOTELOWNER") {
        this.router.navigate(['/login']);
      } else {
        this.hotelIdFromLogin = account.hotelId;
        this.loadData();
      }
    } else {
      this.loginService.checkSession().subscribe(
        (response) => {
          this.loginService.authenticatedLoginDetailsModel.next(response);
          account = response;
          let role = account.role;
          if (role !== "ROLE_HOTELOWNER") {
            this.router.navigate(['']);
          } else {
            this.hotelIdFromLogin = account.hotelId;
            this.loadData();
          }
        }
      )
    }
  }


  private loadData() {
    this.hotelService.getHotelFormData().subscribe(
      (hotelFormData: HotelFormDataModel) => {
        this.hotelTypeOption = hotelFormData.hotelType;
        this.hotelFeatureTypeOption = hotelFormData.hotelFeatures;
        this.createHotelFeaturesCheckboxControl();
      }
    );

    if (this.hotelIdFromLogin) {
      this.isUpdate = true;
      this.getHotelCreateData(String(this.hotelIdFromLogin));
    } else {
      this.isUpdate = false;
    }
  }

  onSubmit() {
    const data = {...this.hotelForm.value};
    data.hotelFeatures = this.createHotelFeaturesArrayToSend();
    this.isUpdate ? this.updateHotel(data) : this.createHotel(data);
  }

  createHotel = (data: HotelCreateItemModel) => {
    this.hotelService.createHotel(data).subscribe(
      (hotelId) => {
        console.log('hotelId:' + hotelId);
        let account = this.loginService.authenticatedLoginDetailsModel.getValue();
        account.hotelId = hotelId;
        this.loginService.authenticatedLoginDetailsModel.next(account);
        this.router.navigate(['/admin/hotel']);
      }, error => validationHandler(error, this.hotelForm),
    );
  };

  getHotelCreateData = (id: string) => {
    this.hotelService.hotelForUpdate(id).subscribe(
      (response: HotelCreateItemModel) => {
        this.hotelForm.patchValue({
          name: response.name,
          postalCode: response.postalCode,
          city: response.city,
          streetAddress: response.streetAddress,
          hotelType: response.hotelType,
          description: response.description,
          hotelFeatures: this.createHotelFeaturesFormArray(response.hotelFeatures),
        });
      },
    );
  };

  private updateHotel(data: HotelCreateItemModel) {
    this.hotelService.updateHotel(data, this.hotelIdFromLogin).subscribe(
      () => {
        this.router.navigate(['/admin/hotel']);
      }, error => validationHandler(error, this.hotelForm),
    );
  }

  private createHotelFeaturesCheckboxControl() {
    this.hotelFeatureTypeOption.forEach(() => {
        const control = new FormControl(false);
        (this.hotelForm.controls.hotelFeatures as FormArray).push(control);
      }
    );
  }

  private createHotelFeaturesArrayToSend(): string[] {
    return this.hotelForm.value.hotelFeatures
      .map((hotelFeatures, index) => hotelFeatures ? this.hotelFeatureTypeOption[index].name : null)
      .filter(hotelFeatures => hotelFeatures !== null);
  }


  createHotelFeaturesFormArray = (hotelFeaturesNames: string[]) => {
    return this.hotelFeatureTypeOption.map(hotelFeatures => {
        return hotelFeaturesNames.includes(hotelFeatures.name);
      }
    );
  };

}
