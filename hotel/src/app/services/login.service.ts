import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {AuthenticatedLoginDetailsModel} from "../models/authenticatedLoginDetails.model";
import {AccountDetailsForMyProfileModel} from "../models/accountDetailsForMyProfile.model";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private BASE_URL = environment.BASE_URL;

  authenticatedLoginDetailsModel = new BehaviorSubject<AuthenticatedLoginDetailsModel>(null);

  constructor(private http: HttpClient) {
  }

  authenticate(credentials): Observable<AuthenticatedLoginDetailsModel> {
    const headers = new HttpHeaders(credentials ? {
      authorization: 'Basic ' + btoa(credentials.email + ':' + credentials.password),
    } : {});
    return this.http.get<AuthenticatedLoginDetailsModel>(this.BASE_URL + '/api/accounts/me', {headers: headers});
  }

  logout() {
    this.authenticatedLoginDetailsModel.next(null);
    this.http.post(this.BASE_URL + '/api/accounts/logout', {}).subscribe();
  }

  activateUser(token: string) {
    return this.http.put<any>(this.BASE_URL + '/api/registrations', token);
  }

  filldatas() {
    return this.http.get<any>(this.BASE_URL + "/admin/fill-database");
  }

  //-------Profilomhoz-------

  getAccountDetails = (email: string): Observable<AccountDetailsForMyProfileModel> => {
    return this.http.get<AccountDetailsForMyProfileModel>(this.BASE_URL + '/api/accounts/' + email);
  };

  checkSession() {
    return this.http.get<AuthenticatedLoginDetailsModel | null>(this.BASE_URL + '/api/accounts/sessionCheck');
  }
}
