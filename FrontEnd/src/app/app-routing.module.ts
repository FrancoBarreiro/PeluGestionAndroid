import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CustomersListComponent } from './components/customers-list/customers-list.component';
import { CustomerRegisterComponent } from './components/customer-register/customer-register.component';
import { CustomerUpdateComponent } from './components/customer-update/customer-update.component';
import { CustomerDetailsComponent } from './components/customer-details/customer-details.component';
import { JobRegisterComponent } from './components/job-register/job-register.component';
import { JobUpdateComponent } from './components/job-update/job-update.component';
import { ResultsJobsComponent} from './components/results-jobs/results-jobs.component';
import { ExpenseRegisterComponent } from './components/expense-register/expense-register.component';
import { ExpensesListComponent } from './components/expenses-list/expenses-list.component';
import { UpdateExpenseComponent } from './components/update-expense/update-expense.component';
import { ResultsExpensesComponent } from './components/results-expenses/results-expenses.component';
import { LoginComponent } from './components/login/login.component';

const routes: Routes = [
  {path: '', component: DashboardComponent},
  {path: 'clientes', component: CustomersListComponent },
  {path: 'clientes/registrar', component: CustomerRegisterComponent},
  {path: 'clientes/actualizar/:id', component:CustomerUpdateComponent},
  {path: 'clientes/detalles/:id', component:CustomerDetailsComponent},
  {path: 'clientes/:id/agregar-trabajo', component:JobRegisterComponent},
  {path: 'trabajos/actualizar/:id', component:JobUpdateComponent},
  {path: 'resultados/trabajos', component:ResultsJobsComponent},
  {path: 'gastos/agregar', component:ExpenseRegisterComponent},
  {path: 'gastos', component:ExpensesListComponent},
  {path: 'gastos/actualizar/:id', component:UpdateExpenseComponent},
  {path: 'resultados/gastos', component:ResultsExpensesComponent},
  {path: 'login', component:LoginComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
