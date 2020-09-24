import {Component, OnInit} from '@angular/core';
import {RoomDetailsModel} from "../../models/roomDetails.model";
import {RoomService} from "../../services/room.service";
import {ActivatedRoute, Router} from "@angular/router";
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-room-details',
  templateUrl: './room-details.component.html',
  styleUrls: ['./room-details.component.css']
})
export class RoomDetailsComponent implements OnInit {

  room: RoomDetailsModel;
  hotelId: number;

  constructor(private roomService: RoomService, private loginService: LoginService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    let account = this.loginService.authenticatedLoginDetailsModel.getValue();
    if (account) {
      this.hotelId = account.hotelId;
    } else {
      this.loginService.checkSession().subscribe(
        (response) => {
          this.loginService.authenticatedLoginDetailsModel.next(response);
          if (response) {
            this.hotelId = response.hotelId;
          } else {
            this.router.navigate([''])
          }
        });
    }


    this.route.paramMap.subscribe(
      paramMap => {
        const roomId = paramMap.get('id');
        if (roomId) {
          this.getRoomDetail(roomId);
        }
      },
      error => console.warn(error),
    );

  }

  getRoomDetail = (roomId: string) => {
    this.roomService.roomDetail(roomId).subscribe(
      (response: RoomDetailsModel) => {
        if (response.hotelId === this.hotelId) {
          this.room = response;
        }
      },
      error => {console.log(error)}
    );
  };

  updateRoom(id: number): void {
    this.router.navigate(['/admin/hotel/update-room/', id])
  }

  backToTheHotel() {
    this.router.navigate(['/admin/hotel'])
  }

}
