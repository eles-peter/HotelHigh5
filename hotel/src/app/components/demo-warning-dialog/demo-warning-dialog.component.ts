import {Component, ViewEncapsulation} from "@angular/core";
import {MatDialogRef} from "@angular/material/dialog";


@Component({
  selector: 'app-demo-warning-dialog',
  templateUrl: './demo-warning-dialog.component.html',
  styleUrls: ['./demo-warning-dialog.component.css'],
  encapsulation: ViewEncapsulation.None
})

export class DemoWarningDialogComponent {

  constructor(public dialogRef: MatDialogRef<DemoWarningDialogComponent>) {
  }

  closeDialog() {
    this.dialogRef.close();
  }

}
