import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {validationHandler} from "../../../utils/validationHandler";
import {RegistrationService} from "../../../services/registration.service";
import {AccountDetailsForMyProfileModel} from "../../../models/accountDetailsForMyProfile.model";
import {LoginService} from "../../../services/login.service";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-account-edit',
  templateUrl: './account-edit.component.html',
  styleUrls: ['./account-edit.component.css']
})
export class AccountEditComponent implements OnInit {
  registerForm: FormGroup;
  account: AccountDetailsForMyProfileModel;
  user;

  constructor(private loginService: LoginService, private registrationService: RegistrationService, private router: Router,
              private notificationService: NotificationService) {
    this.registerForm = new FormGroup({
      'firstname': new FormControl("", Validators.required),
      'lastname': new FormControl("", Validators.required),
      'address': new FormControl("", Validators.required),
    });
  }

  ngOnInit() {
    let account = this.loginService.authenticatedLoginDetailsModel.getValue();
    if (account) {
      this.loginService.getAccountDetails(account.name).subscribe((response) => {
        this.account = response;
        this.registerForm.patchValue({
          firstname: response.firstname,
          lastname: response.lastname,
          address: response.address
        })
      });
    } else {
      this.loginService.checkSession().subscribe(
        (response) => {
          this.loginService.authenticatedLoginDetailsModel.next(response);
          if (response) {
            this.loginService.getAccountDetails(response.name).subscribe((response) => {
              this.account = response;
              this.registerForm.patchValue({
                firstname: response.firstname,
                lastname: response.lastname,
                address: response.address
              })
            });
          } else {
            this.router.navigate([''])
          }
        });
    }
  }

  doSaveModifiedAccount() {
    this.registrationService.updateUserAccount(this.registerForm.value, this.account.username).subscribe(
      () => {
        this.notificationService.success('A változtatásaid elmentettük!');
        this.router.navigate(['/account']);
      },
      error => {
        validationHandler(error, this.registerForm);
      }
    )
  }
}
