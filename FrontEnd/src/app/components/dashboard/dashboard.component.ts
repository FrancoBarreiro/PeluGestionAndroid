import { Component, OnInit, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private elementRef: ElementRef, private loginService:LoginService, private router:Router) { }

  ngOnInit() {
    if (this.loginService.isLoggedIn()) {
      /*var s = document.createElement("script");
      s.type = "text/javascript";
      s.src = "../assets/js/main.js";
      this.elementRef.nativeElement.appendChild(s);*/
    } else {
      this.router.navigate(['login']);
    }
  }
}
