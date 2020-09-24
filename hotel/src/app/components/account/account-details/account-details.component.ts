import {Component, OnInit} from '@angular/core';
import {AccountDetailsForMyProfileModel} from "../../../models/accountDetailsForMyProfile.model";
import {LoginService} from "../../../services/login.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {

  account: AccountDetailsForMyProfileModel;

  constructor(private loginService: LoginService, private router: Router) {
  }

  ngOnInit() {
    let account = this.loginService.authenticatedLoginDetailsModel.getValue();
    if (account) {
      this.loginService.getAccountDetails(account.name).subscribe((response) => {
        this.account = response
      });
    } else {
      this.loginService.checkSession().subscribe(
        (response) => {
          this.loginService.authenticatedLoginDetailsModel.next(response);
          if (response) {
            this.loginService.getAccountDetails(response.name).subscribe((response) => {
              this.account = response
            });
          } else {
            this.router.navigate([''])
          }
        });
    }
  }
}
