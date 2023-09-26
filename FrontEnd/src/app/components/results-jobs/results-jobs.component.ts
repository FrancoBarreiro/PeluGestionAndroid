import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from 'src/app/models/customer';
import { Job } from 'src/app/models/job';
import { CustomerService } from 'src/app/services/customer.service';
import { JobService } from 'src/app/services/job.service';
import * as moment from 'moment';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-statistics-jobs',
  templateUrl: './results-jobs.component.html',
  styleUrls: ['./results-jobs.component.css']
})
export class ResultsJobsComponent {

  customer: Customer = new Customer();
  customersList: Customer[];

  job: Job;
  jobsList: Job[];
  dateFrom: string;
  dateTo: string;
  size: number = 10;
  page: number = 0;
  currentPage = 1;
  totalPages = 1;
  pageRange: number[] = [];
  totalJobsCount = 0;
  jobsPerPage = 10;
  isFirstPage: boolean = true;
  selectedJob: Job | null = null;
  sumOfPeriod: number;

  constructor(private jobService: JobService, private router: Router, private activatedRoute: ActivatedRoute, private loginService: LoginService) { }

  ngOnInit() {
    if (this.loginService.isLoggedIn()) {
      const today = new Date();
      const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

      this.dateFrom = this.formatDateAsString(firstDayOfMonth);
      this.dateTo = this.formatDateAsString(today);
      this.getJobsByDates();
    } else {
      this.router.navigate(['login']);
    }
  }

  viewJobDetails(jobId: Number) {
    const job = this.jobsList.find(j => j.idJob === jobId);
    if (job) {
      this.selectedJob = job;
    }
  }

  getJobsByDates() {
    const formattedDateFrom = this.formatDate(this.dateFrom);
    const formattedDateTo = this.formatDate(this.dateTo);

    this.jobService.getJobsFromDateToDate(this.page, this.size, formattedDateFrom, formattedDateTo).subscribe(
      jobsFound => {
        this.jobsList = jobsFound.content;
        this.totalJobsCount = jobsFound.totalElements;
        this.totalPages = Math.ceil(this.totalJobsCount / this.jobsPerPage);
        this.pageRange = this.calculatePageRange(this.totalPages, this.currentPage);
        this.isFirstPage = this.currentPage === 1; // Verificar si la página actual es la primera
      },
      error => {
        console.log(error);
      }
    );

    this.jobService.getSumTotalByPeriod(formattedDateFrom, formattedDateTo).subscribe(
      sum => {
        this.sumOfPeriod = sum;
        console.log(this.sumOfPeriod)
      },
      error => console.log(error)
    );
  }

  goToPage(pageNumber: number) {
    this.currentPage = pageNumber;
    this.page = pageNumber - 1; // Ajustar el número de página para la solicitud a la API
    this.getJobsByDates();
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
    const maxVisiblePages = 5; // Define cuántos números de página se mostrarán en el rango visible

    let start = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let end = Math.min(start + maxVisiblePages - 1, totalPages);

    // Asegurarse de que el rango no se extienda más allá del número total de páginas
    if (end - start < maxVisiblePages - 1) {
      start = Math.max(1, end - maxVisiblePages + 1);
    }

    for (let i = start; i <= end; i++) {
      range.push(i);
    }

    return range;
  }

  onPageSizeChange() {
    this.page = 0; // Reiniciar la página a la primera al cambiar el tamaño de datos por página
    this.currentPage = 1; // Reiniciar la página actual a la primera al cambiar el tamaño de datos por página
    this.jobsPerPage = this.size; // Actualizar el número de trabajos por página
    this.getJobsByDates();
  }

}
