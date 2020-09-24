import {Component, Inject, OnInit, ViewEncapsulation} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RegistrationService} from "../../../services/registration.service";
import {validationHandler} from "../../../utils/validationHandler";
import {NotificationService} from "../../../services/notification.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class RegistrationComponent implements OnInit {
  registerForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<RegistrationComponent>, private registrationService: RegistrationService,
              private router: Router, private notificationService: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data) {
    this.registerForm = new FormGroup({
      'email': new FormControl("", Validators.required),
      'password': new FormControl("", Validators.required),
      'firstname': new FormControl("", Validators.required),
      'lastname': new FormControl("", Validators.required),
      'address': new FormControl("", Validators.required),
    });
  }

  doRegistrationAsUser() {
    this.registrationService.sendUserRegistration(this.registerForm.value).subscribe(
      () => {
        this.notificationService.success('Aktiváló kódot küldtünk a megadott email címedre!');
        // this.router.navigate(['/login']);
        this.closeDialog();
      },
      errors => {
        validationHandler(errors, this.registerForm);
      }
    );
  }

  ngOnInit() {
  }

  closeDialog() {
    this.dialogRef.close(true);
  }

  doRegistrationAsHotelOwner() {
    this.registrationService.sendHotelOwnerRegistration(this.registerForm.value).subscribe(
      () => {
        this.notificationService.success('Aktiváló kódot küldtünk a megadott email címedre!');
        this.closeDialog();
      },
      errors => {
        validationHandler(errors, this.registerForm);
      }
    );
  }
}
