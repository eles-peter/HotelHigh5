import {Component, Inject, OnInit, ViewEncapsulation} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {LoginService} from "../../../services/login.service";
import {RegistrationService} from "../../../services/registration.service";
import {validationHandler} from "../../../utils/validationHandler";
import {NotificationService} from "../../../services/notification.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {RegistrationComponent} from "../registration/registration.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<LoginComponent>, private loginService: LoginService, private router: Router,
              private regService: RegistrationService, private notificationService: NotificationService,
              private dialog: MatDialog, @Inject(MAT_DIALOG_DATA) public data) {
    this.loginForm = new FormGroup({
      'email': new FormControl('', Validators.required),
      'password': new FormControl('', Validators.required),
    });
  }

  ngOnInit(): void {
  }

  doLogin() {
    const data = {...this.loginForm.value};
    this.loginService.authenticate(data).subscribe(
      response => {
        this.loginService.authenticatedLoginDetailsModel.next(response);
        if (response.role == "ROLE_HOTELOWNER") {
          this.router.navigate(['admin/hotel'])
        }
        this.notificationService.success('Sikeresen beléptél!');
        this.closeDialog();
      },
      error => {
        this.notificationService.unsuccessful('Rossz email cím vagy jelszó!');
        this.closeDialog();
        this.loginForm.reset();
        error.error = {
          fieldErrors: [
            {
              field: 'userEmail',
              message: 'Invalid email or password',
            },
          ],
        };
        validationHandler(error, this.loginForm);
      });

    return false;
  }

  closeDialog() {
    this.dialogRef.close(true);
  }

  doRegistration() {
    this.dialog.open(RegistrationComponent, {
      height: '600px',
      width: '400px',
      data: {
        registrationType: 'asUser'
      }
    });
  }


}
