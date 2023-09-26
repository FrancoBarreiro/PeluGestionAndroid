import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { Expense } from 'src/app/models/expense';
import { ExpenseService } from 'src/app/services/expense.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-results-expenses',
  templateUrl: './results-expenses.component.html',
  styleUrls: ['./results-expenses.component.css']
})
export class ResultsExpensesComponent {

  expense: Expense = new Expense();
  expensesList: Expense[];
  dateFrom: string;
  dateTo: string;
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

  constructor(private expenseService: ExpenseService, private router: Router, private activatedRoute: ActivatedRoute, private loginService:LoginService) { }

  ngOnInit() {

    if (this.loginService.isLoggedIn()) {
      const today = new Date();
      const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

      this.dateFrom = this.formatDateAsString(firstDayOfMonth);
      this.dateTo = this.formatDateAsString(today);
      this.getExpensesByDates();
    } else {
      this.router.navigate(['login']);
    }

  }

  viewExpenseDetails(expenseId: Number) {
    const expense = this.expensesList.find(e => e.id === expenseId);
    if (expense) {
      this.selectedExpense = expense;
    }
  }

  getExpensesByDates() {
    const formattedDateFrom = this.formatDate(this.dateFrom);
    const formattedDateTo = this.formatDate(this.dateTo);

    this.expenseService.getExpensesFromDateToDate(this.page, this.size, formattedDateFrom, formattedDateTo).subscribe(
      expensesFound => {
        this.expensesList = expensesFound.content;
        this.totalExpensesCount = expensesFound.totalElements;
        this.totalPages = Math.ceil(this.totalExpensesCount / this.expensesPerPage);
        this.pageRange = this.calculatePageRange(this.totalPages, this.currentPage);
        this.isFirstPage = this.currentPage === 1;
      },
      error => {
        console.log(error);
      }
    );

    this.expenseService.getSumTotalByPeriod(formattedDateFrom, formattedDateTo).subscribe(
      sum => {
        this.sumOfPeriod = sum;
        console.log(this.sumOfPeriod)
      },
      error => console.log(error)
    );
  }

  goToPage(pageNumber: number) {
    this.currentPage = pageNumber;
    this.page = pageNumber - 1;
    this.getExpensesByDates();
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

  private formatDate(dateString: string): string {
    const parts = dateString.split('-');
    const day = parts[2];
    const month = parts[1];
    const year = parts[0];

    const formattedDate = `${day}-${month}-${year} 00:00:00`;

    return formattedDate;
  }

  private formatDateAsString(date: Date): string {
    const year = date.getFullYear().toString();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  public formatDateForDisplay(dateString: string): string {
    const formattedDate = moment(dateString, 'DD-MM-YYYY HH:mm:ss').format('DD/MM/YYYY');
    return formattedDate;
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

  onPageSizeChange() {
    this.page = 0;
    this.currentPage = 1;
    this.expensesPerPage = this.size;
    this.getExpensesByDates();
  }
}
