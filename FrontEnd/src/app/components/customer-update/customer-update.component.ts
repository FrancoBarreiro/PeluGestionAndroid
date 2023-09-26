import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from 'src/app/models/customer';
import { CustomerService } from 'src/app/services/customer.service';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-customer-update',
  templateUrl: './customer-update.component.html',
  styleUrls: ['./customer-update.component.css']
})
export class CustomerUpdateComponent {

  customer: Customer = new Customer();

  constructor(private customerService: CustomerService, private router: Router, private activatedRoute: ActivatedRoute, private loginService: LoginService) { }

  ngOnInit() {
    if (this.loginService.isLoggedIn()) {
      this.customer.id = this.activatedRoute.snapshot.params['id'];
      this.customerService.getCustomerById(this.customer.id).subscribe(
        customerFromBD => {
          this.customer.id = customerFromBD.id;
          this.customer.name = customerFromBD.name;
          this.customer.surname = customerFromBD.surname;
          this.customer.cellphone = customerFromBD.cellphone;
        }, error => console.log(error));
    } else {
      this.router.navigate(['login']);
    }

  }

  public updateCustomer() {
    Swal.fire({
      title: 'Desea actualizar el cliente?',
      showDenyButton: true,
      confirmButtonText: 'Guardar',
      denyButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.customerService.updateCustomer(this.customer).subscribe(
          response => {
            console.log("El cliente se ha actualizado correctamente.", response);
          },
          error => {
            console.error("Ocurrió un error al actualizar el cliente.", error);
          });
        this.router.navigate(['clientes/detalles/' + this.customer.id]);
        Swal.fire('Guardado!', '', 'success')
        this.router.navigate(['clientes'],{ queryParams:  { registroExitoso: true }});
      } else if (result.isDenied) {
        Swal.fire('El cliente NO ha sido actualizado', '', 'info')
      }
    })
  }
}
