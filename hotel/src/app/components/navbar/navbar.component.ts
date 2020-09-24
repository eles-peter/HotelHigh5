import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {LoginService} from "../../services/login.service";
import {MatDialog} from "@angular/material/dialog";
import {LoginComponent} from "../account/login/login.component";
import {RegistrationComponent} from "../account/registration/registration.component";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  loggedIn: boolean;
  userRole: string;
  email: string;
  baseRouterLink: string = '';
  lastname: string;

  isWindowOpen: boolean;

  constructor(private http: HttpClient, private router: Router, private loginService: LoginService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.loginService.authenticatedLoginDetailsModel.subscribe(
      (response) => {
        if (response !== null) {
          this.loggedIn = true;
          this.userRole = response.role;
          this.email = response.name;
          this.lastname = response.lastname;
          if (this.userRole === 'ROLE_HOTELOWNER') {
            this.baseRouterLink = 'admin/hotel';
          }
        }
      }
    );
  }

  logout() {
    this.loggedIn = false;
    this.userRole = null;
    this.baseRouterLink = '';
    this.router.navigateByUrl('').then(() => this.loginService.logout()
    );
  }

  doRegistration(registrationType: string) {
    if (this.isWindowOpen) {
      return
    }
    this.isWindowOpen = true;
    const dialogRef = this.dialog.open(RegistrationComponent, {
      height: '600px',
      width: '400px',
      data: {
        registrationType: registrationType
      }
    });
    dialogRef.afterClosed().subscribe(() => {
      this.isWindowOpen = false
    })
  }

  doLogin() {
    if (this.isWindowOpen) {
      return
    }
    this.isWindowOpen = true;
    const dialogRef = this.dialog.open(LoginComponent, {
      height: '400px',
      width: '400px',
      data: {
        openedBy: 'navbar'
      }
    });
    dialogRef.afterClosed().subscribe(() => {
      this.isWindowOpen = false
    })
  }

}
