import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Expense } from 'src/app/models/expense';
import { ExpenseService } from 'src/app/services/expense.service';
import Swal from 'sweetalert2';
import { DatePipe } from '@angular/common';
import { LoginService } from 'src/app/services/login.service';



@Component({
  selector: 'app-expense-register',
  templateUrl: './expense-register.component.html',
  styleUrls: ['./expense-register.component.css'],
  providers: [DatePipe]
})
export class ExpenseRegisterComponent {

  expense: Expense = new Expense();


  constructor(private router: Router, private expenseService: ExpenseService, private datePipe: DatePipe, private loginService: LoginService) { }

  ngOnInit() {
    if (!this.loginService.isLoggedIn()) {
      this.router.navigate(['login']);
    }
  }

  addExpense() {
    const partesFecha = this.expense.date.split('-');
    this.expense.date = partesFecha[2] + '-' + partesFecha[1] + '-' + partesFecha[0] + ' 00:00:00';
    this.expenseService.saveExpense(this.expense).subscribe(
      response => {
        this.expense = response;
        this.router.navigate(['gastos']);
        Swal.fire(
          'Guardado!',
          'Se registró correctamente el gasto!',
          'success'
        )
      },
      error => console.log(error)
    );
  }

}
