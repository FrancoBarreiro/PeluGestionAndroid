import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { Customer } from 'src/app/models/customer';
import { Job } from 'src/app/models/job';
import { CustomerService } from 'src/app/services/customer.service';
import { JobService } from 'src/app/services/job.service';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrls: ['./customer-details.component.css']
})
export class CustomerDetailsComponent {

  customer: Customer = new Customer();
  jobsList:Job[];
  job: Job = new Job();
  size: number = 10;
  page: number = 0;
  currentPage = 1;
  totalPages = 1;
  pageRange: number[] = [];
  totalJobsCount = 0;
  jobsPerPage = 10;
  isFirstPage: boolean = true;
  selectedJob: Customer | null = null;

  constructor(private customerService: CustomerService, private jobService: JobService, private router: Router, private activatedRoute: ActivatedRoute, private loginService: LoginService) { }

  ngOnInit() {

    if (this.loginService.isLoggedIn()) {
      this.customer.id = this.activatedRoute.snapshot.params['id'];
      this.customerService.getCustomerDetails(this.customer.id).subscribe(
        customerFound => {
          this.customer.name = customerFound.name;
          this.customer.surname = customerFound.surname;
          this.customer.cellphone = customerFound.cellphone;
        },
        error => console.log(error))
      this.getCustomer();
      this.getJobsByCustomerId();
    } else {
      this.router.navigate(['login']);
    }

  }

  private getCustomer() {
    this.customerService.getCustomerById(this.customer.id).subscribe(
      customerFound => {
        this.customer = customerFound;
      },
      error => console.log(error))
  }

  public viewJobDetails(id: Number) {
    this.jobService.getJobById(id).subscribe(
      jobFound => {
        this.job = jobFound;
      },
      error => console.log(error))
  }

  public redirectToUpdate(id: Number) {
    this.router.navigate(['clientes/actualizar', id]);
  }

  public redirectToUpdateJob(idJob: Number) {
    this.router.navigate(['trabajos/actualizar', idJob]);
  }

  deleteJob(idJob: Number) {
    Swal.fire({
      title: 'Estás seguro que quieres eliminar el trabajo?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      cancelButtonText: 'Cancelar',
      confirmButtonText: 'Sí, eliminar!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.jobService.deleteJob(idJob).subscribe(
          response => {
            Swal.fire(
              'Eliminado',
              'El el trabajo ha sido eliminado.',
              'success'
            )
            this.ngOnInit();
          },
          error => console.log(error))
      }
    })
  }

  getJobsByCustomerId(){
    this.jobService.getJobsByCustomerId(this.size,this.page,this.customer.id).subscribe(
      response => {
        this.jobsList = response.content;
        this.totalJobsCount = response.totalElements;
        this.totalPages = Math.ceil(this.totalJobsCount / this.jobsPerPage);
        this.pageRange = this.calculatePageRange(this.totalPages, this.currentPage);
        this.isFirstPage = this.currentPage === 1;
      },
      error => {
        console.log(error);
      }
    )
  }

  goToPage(pageNumber: number) {
    this.currentPage = pageNumber;
    this.page = pageNumber - 1; // Ajustar el número de página para la solicitud a la API
    this.getJobsByCustomerId();
  }

  onPageSizeChange() {
    this.page = 0; // Reiniciar la página a la primera al cambiar el tamaño de datos por página
    this.currentPage = 1; // Reiniciar la página actual a la primera al cambiar el tamaño de datos por página
    this.jobsPerPage = this.size; // Actualizar el número de trabajos por página
    this.getJobsByCustomerId();
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

  public formatDateForDisplay(dateString: string): string {
    const formattedDate = moment(dateString, 'DD-MM-YYYY HH:mm:ss').format('DD/MM/YYYY');
    return formattedDate;
  }

}


