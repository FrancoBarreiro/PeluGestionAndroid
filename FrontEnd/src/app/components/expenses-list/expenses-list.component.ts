import { Component } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { Expense } from 'src/app/models/expense';
import { ExpenseService } from 'src/app/services/expense.service';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-expenses-list',
  templateUrl: './expenses-list.component.html',
  styleUrls: ['./expenses-list.component.css']
})
export class ExpensesListComponent {

  expense: Expense = new Expense();
  expensesList: Expense[];
  size: number = 10;
  page: number = 0;
  currentPage = 1;
  totalPages = 1;
  pageRange: number[] = [];
  totalExpensesCount = 0;
  expensesPerPage = 10;
  isFirstPage: boolean = true;
  selectedExpense: Expense | null = null;
  sumOfPeriod: number;

  constructor(private expenseService: ExpenseService, private router: Router, private loginService: LoginService) { }

  ngOnInit() {
    if (this.loginService.isLoggedIn()) {
      this.fetchExpenses();
    } else {
      this.router.navigate(['login']);
    }
  }

  fetchExpenses() {
    this.getExpenses();
  }

  deleteExpense(id: Number) {
    Swal.fire({
      title: 'Estás seguro que quieres eliminar el gasto?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      cancelButtonText: 'Cancelar',
      confirmButtonText: 'Sí, eliminar!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.expenseService.deleteExpense(id).subscribe(
          response => {
            Swal.fire(
              'Eliminado',
              'El gasto ha sido eliminado.',
              'success'
            )
            this.fetchExpenses();
          },
          error => console.log(error))

      }
    })
  }

  redirectToUpdateExpense(id: Number) {
    this.router.navigate(['gastos/actualizar', id]);
  }

  getExpenses() {
    this.expenseService.getExpenses(this.page, this.size).subscribe(
      response => {
        this.expensesList = response.content;
        this.totalExpensesCount = response.totalElements;
        this.totalPages = Math.ceil(this.totalExpensesCount / this.expensesPerPage);
        this.pageRange = this.calculatePageRange(this.totalPages, this.currentPage);
        this.isFirstPage = this.currentPage === 1;
      },
      error => { console.log(error) }
    )
  }

  calculatePageRange(totalPages: number, currentPage: number): number[] {
    const range = [];
    const maxVisiblePages = 5;

    let start = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let end = Math.min(start + maxVisiblePages - 1, totalPages);

    if (end - start < maxVisiblePages - 1) {
      start = Math.max(1, end - maxVisiblePages + 1);
    }

    for (let i = start; i <= end; i++) {
      range.push(i);
    }

    return range;
  }

  viewExpenseDetails(expenseId: Number) {
    const expense = this.expensesList.find(e => e.id === expenseId);
    if (expense) {
      this.selectedExpense = expense;
    }
  }

  goToPage(pageNumber: number) {
    this.currentPage = pageNumber;
    this.page = pageNumber - 1;
    this.getExpenses();
  }

  goToPreviousPage() {
    if (this.currentPage > 1) {
      this.goToPage(this.currentPage - 1);
    }
  }

  goToNextPage() {
    if (this.currentPage < this.totalPages) {
      this.goToPage(this.currentPage + 1);
    }
  }

  formatDateForDisplay(dateString: string): string {
    const formattedDate = moment(dateString, 'DD-MM-YYYY HH:mm:ss').format('DD/MM/YYYY');
    return formattedDate;
  }

  onPageSizeChange() {
    this.page = 0; // Reiniciar la página a la primera al cambiar el tamaño de datos por página
    this.currentPage = 1; // Reiniciar la página actual a la primera al cambiar el tamaño de datos por página
    this.expensesPerPage = this.size; // Actualizar el número de trabajos por página
    this.getExpenses();
  }

}
