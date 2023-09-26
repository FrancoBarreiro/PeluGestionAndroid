import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { LoginData } from 'src/app/models/login-data';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  loginData: LoginData = new LoginData();
  isValid: boolean;

  constructor(private loginService: LoginService, private router: Router, private app: AppComponent) { }

  ngOnInit() {
    this.isLoggedIn();
  }

  isLoggedIn() {
    if (this.loginService.isLoggedIn()) {
      this.router.navigate(['']);
    }
  }

  verifyUser() {
    let tokenStr = localStorage.getItem('token');
    if(tokenStr != undefined || tokenStr != '' || tokenStr != null){
      localStorage.removeItem('token');
    }
    this.loginService.verifyUser(this.loginData).subscribe(
      response => {
        this.app.isLoggedIn = true;
        const token = response.token;
        this.loginService.loginUser(token);
        this.router.navigate(['']);

      },
      error => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Usuario y/o contraseña incorrecta!',
        })
        console.log(error);
      }
    )
  }

}
