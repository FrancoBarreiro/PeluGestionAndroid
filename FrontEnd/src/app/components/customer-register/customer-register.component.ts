import { Component, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { Customer } from 'src/app/models/customer';
import { CustomerService } from 'src/app/services/customer.service';
import { LoginService } from 'src/app/services/login.service';
import Swal from 'sweetalert2';
import { CustomerUpdateComponent } from '../customer-update/customer-update.component';
import { CustomerUpdateService } from 'src/app/services/customer-update.service';

@Component({
  selector: 'app-customer-register',
  templateUrl: './customer-register.component.html',
  styleUrls: ['./customer-register.component.css']
})
export class CustomerRegisterComponent {

  customer: Customer = new Customer;

  constructor(private customerService: CustomerService, private router: Router, private loginService: LoginService,
    private customerUpdateService: CustomerUpdateService) { }

  ngOnInit() {
    if (!this.loginService.isLoggedIn()) {
      this.router.navigate(['login']);
    }
  }


  public registerCustomer() {
    Swal.fire({
      title: 'Si continúas se guardará al cliente?',
      showDenyButton: true,
      confirmButtonText: 'Guardar',
      denyButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.customerService.saveCustomer(this.customer).subscribe(
          response => {
            this.customerUpdateService.customerRegistered.emit();
            console.log("El cliente se ha registrado correctamente.", response);
          },
          error => {
            console.error("Ocurrió un error al registrar el cliente.", error);
          });
        Swal.fire('Registrado!', '', 'success')
        this.router.navigate(['clientes'],{ queryParams:  { registroExitoso: true }});
      } else if (result.isDenied) {
        Swal.fire('El cliente no ha sido guardado', '', 'info')
      }
    })
  }

}
