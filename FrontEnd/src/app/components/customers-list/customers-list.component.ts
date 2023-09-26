import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Customer } from 'src/app/models/customer';
import { CustomerUpdateService } from 'src/app/services/customer-update.service';
import { CustomerService } from 'src/app/services/customer.service';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.css']
})
export class CustomersListComponent {

  customer: Customer;
  customersList: Customer[];
  nameCustomerToSearch: String;
  size: number = 10;
  page: number = 0;
  currentPage = 1;
  totalPages = 1;
  pageRange: number[] = [];
  totalCustomersCount = 0;
  customersPerPage = 10;
  isFirstPage: boolean = true;
  selectedCustomer: Customer | null = null;

  constructor(private customerService: CustomerService, private router: Router, private loginService: LoginService,
    private activatedRoute: ActivatedRoute, private customerUpdateService: CustomerUpdateService) { }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['registroExitoso']) {
        this.getCustomers();
      }
    });

    this.customerUpdateService.customerRegistered.subscribe(() => {
      this.getCustomers();
    });

    if (this.loginService.isLoggedIn()) {
      this.getCustomers();
    } else {
      this.router.navigate(['login']);
    }
  }


  private getCustomers() {
    this.customerService.getListCustomers(this.size, this.page).subscribe(
      customersFound => {
        this.customersList = customersFound.content;
        this.totalCustomersCount = customersFound.totalElements;
        this.totalPages = Math.ceil(this.totalCustomersCount / this.customersPerPage);
        this.pageRange = this.calculatePageRange(this.totalPages, this.currentPage);
        this.isFirstPage = this.currentPage === 1; // Verificar si la página actual es la primera
      });
  }

  public deleteCustomer(id: Number) {
    Swal.fire({
      title: 'Si elimina al cliente se eliminarán todos sus trabajos!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      cancelButtonText: 'Cancelar',
      confirmButtonText: 'Sí, eliminar!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.customerService.deleteCustomer(id).subscribe(
          response => {
            Swal.fire(
              'Eliminado',
              'El cliente ha sido eliminado.',
              'success'
            )
            this.ngOnInit();
          },
          error => console.log(error))

      }
    })
  }

  public searchCustomerByName() {
    if (this.nameCustomerToSearch) {
      this.customerService.getCustomerByName(this.nameCustomerToSearch).subscribe(
        customerFound => {
          this.customersList = customerFound;
        }
      )
    } else {
      this.getCustomers(); // Obtiene la lista completa de clientes
    }
  }

  public handleBlur(): void {
    if (!this.nameCustomerToSearch) {
      this.getCustomers(); // Obtiene la lista completa de clientes si el campo de búsqueda está vacío
    }
  }

  public updateCustomer(id: Number) {
    this.router.navigate(['clientes/actualizar-cliente', id]);
  }

  public redirectToUpdate(id: Number) {
    this.router.navigate(['clientes/actualizar', id]);
  }

  public redirectToDetails(id: Number) {
    this.router.navigate(['clientes/detalles/', id])
  }

  public redirectToAddJob(id: Number) {
    this.router.navigate(['clientes', id, 'agregar-trabajo'])
  }

  goToPage(pageNumber: number) {
    this.currentPage = pageNumber;
    this.page = pageNumber - 1; // Ajustar el número de página para la solicitud a la API
    this.getCustomers();
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

  onPageSizeChange() {
    this.page = 0; // Reiniciar la página a la primera al cambiar el tamaño de datos por página
    this.currentPage = 1; // Reiniciar la página actual a la primera al cambiar el tamaño de datos por página
    this.customersPerPage = this.size; // Actualizar el número de trabajos por página
    this.getCustomers();
  }
}
