import {Component, Input, OnInit} from '@angular/core';
import {HotelService} from "../../../services/hotel.service";
import {getPublicId} from "../../../utils/cloudinaryPublicIdHandler";

@Component({
  selector: 'app-hotel-image-manager',
  templateUrl: './hotel-image-manager.component.html',
  styleUrls: ['./hotel-image-manager.component.css']
})
export class HotelImageManagerComponent implements OnInit {
  @Input() hotelIdFromLogin: number;
  imageURLs: Array<string>;

  constructor(private hotelService: HotelService) {
  }

  ngOnInit() {
    this.hotelService.getHotelImages(this.hotelIdFromLogin).subscribe(
      (response) => this.imageURLs = response
    );

  }

  onFileChange(event) {
    if (event.target.value.length > 0) {
      const file: File = event.target.files[0];
      const formData = new FormData();
      formData.append("file", file);
      this.hotelService.uploadImage(formData, this.hotelIdFromLogin).subscribe(
        () => {
        },
        error => console.log(error),
        () => {
          this.hotelService.getHotelImages(this.hotelIdFromLogin).subscribe(
            (response) => this.imageURLs = response
          );
        });
    }
  }

  getPublicId(imgURL: string) {
    return getPublicId(imgURL);
  }

  deleteImage(image: string) {
    this.hotelService.deleteImage(image, this.hotelIdFromLogin).subscribe(
      () => this.ngOnInit()
    )
  }
}
