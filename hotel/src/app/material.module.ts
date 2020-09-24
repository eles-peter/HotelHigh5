import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {MatDialogModule} from "@angular/material/dialog";
import {MatSnackBarModule} from "@angular/material/snack-bar";


@NgModule({
  declarations: [],
  exports: [FormsModule, MatDialogModule, MatButtonModule, MatInputModule, MatSnackBarModule],
  imports: [
    CommonModule,
    MatSnackBarModule
  ]
})
export class MaterialModule {
}
