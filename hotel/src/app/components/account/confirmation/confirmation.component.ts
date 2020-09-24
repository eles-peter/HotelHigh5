import {Component, OnInit} from "@angular/core";
import {LoginService} from "../../../services/login.service";
import {ActivatedRoute} from "@angular/router";
import {LoginComponent} from "../login/login.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit {

  token: string;
  isActivated: boolean = false;

  constructor(private loginService: LoginService, private route: ActivatedRoute, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      paramMap => {
        const tokenFromRoute = paramMap.get('token');
        console.log(tokenFromRoute);
        if (tokenFromRoute) {
          this.token = tokenFromRoute;
          this.activateUser();
        }
      },
      error => console.warn(error),
    );
    this.dialog.open(LoginComponent, {
      height: '600px',
      width: '400px',
      data: {
        openedBy: 'navbar'
      }
    });
  }

  private activateUser() {
    this.loginService.activateUser(this.token).subscribe(
      () => {
        this.isActivated = true;
      },
      error => console.warn(error),
    );
  }
}
