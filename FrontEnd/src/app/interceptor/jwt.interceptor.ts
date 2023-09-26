import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { LoginService } from '../services/login.service';
import Swal from 'sweetalert2';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private router: Router, private loginService: LoginService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    let request = req;

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(request).pipe(
      catchError((error: any) => {
        if (error.status === 403) {
          Swal.fire({
            title: 'Su sesión ha expirado, por favor inicie nuevamente',
            showClass: {
              popup: 'animate__animated animate__fadeInDown'
            },
            hideClass: {
              popup: 'animate__animated animate__fadeOutUp'
            }
          })
          this.loginService.logout();
          this.router.navigate(['login'])
        }
        return throwError(error);
      })
    );
  }
}
