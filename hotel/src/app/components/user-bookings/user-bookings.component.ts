import {Component, OnInit} from "@angular/core";
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'user-bookings',
  templateUrl: './user-bookings.component.html',
  styleUrls: ['./user-bookings.component.css']
})
export class UserBookingsComponent implements OnInit {

  userId = new BehaviorSubject<number>(0);

  constructor(private loginService: LoginService, private router: Router) {
  }

  ngOnInit(): void {
    let account = this.loginService.authenticatedLoginDetailsModel.getValue();
    if (account) {
      this.userId.next(account.id);
    } else {
      this.loginService.checkSession().subscribe(
        (response) => {
          if (response) {
            this.loginService.authenticatedLoginDetailsModel.next(response);
            this.userId.next(response.id);
          } else {
            this.router.navigate([''])
          }
        }
      )
    }
  }

}
