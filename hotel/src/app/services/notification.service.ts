import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarConfig} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  config: MatSnackBarConfig = {
    duration: 1000,
    horizontalPosition: 'center',
    verticalPosition: 'bottom'
  };

  constructor(public snackBar: MatSnackBar) {
  }

  success(msg) {
    this.config['panelClass'] = ['notification', 'success'];
    this.snackBar.open(msg, '', this.config);
  }

  unsuccessful(msg) {
    this.config['panelClass'] = ['notification', 'unsuccessful'];
    this.snackBar.open(msg, '', this.config);
  }
}
