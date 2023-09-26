import { Component, OnInit } from '@angular/core';
import { LoginService } from './services/login.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'FrontEnd';
  isLoggedIn: boolean;
  logoutSubscription: Subscription;



  constructor(private loginService: LoginService) { }

  ngOnInit() {
    this.isLoggedIn = this.loginService.isLoggedIn();
  
    this.logoutSubscription = this.loginService.logoutNotifier.subscribe((logout) => {
      if (logout) {
        this.isLoggedIn = false;
      }
    });
  }
  
  ngOnDestroy() {
    this.logoutSubscription.unsubscribe();
  }
}


/*intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    let request = req;

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }*/ 
