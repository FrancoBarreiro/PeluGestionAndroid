import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { FooterComponent } from './components/footer/footer.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CustomersListComponent } from './components/customers-list/customers-list.component';
import {HttpClientModule}  from '@angular/common/http';
import { CustomerRegisterComponent } from './components/customer-register/customer-register.component';
import { FormsModule } from '@angular/forms';
import { CustomerUpdateComponent } from './components/customer-update/customer-update.component';
import { RouterModule } from '@angular/router';
import { CustomerDetailsComponent } from './components/customer-details/customer-details.component';
import { JobRegisterComponent } from './components/job-register/job-register.component';
import { JobUpdateComponent } from './components/job-update/job-update.component';
import { CommonModule } from '@angular/common';
import { ResultsJobsComponent } from './components/results-jobs/results-jobs.component';
import { ExpenseRegisterComponent } from './components/expense-register/expense-register.component';
import { ExpensesListComponent } from './components/expenses-list/expenses-list.component';
import { UpdateExpenseComponent } from './components/update-expense/update-expense.component';
import { ResultsExpensesComponent } from './components/results-expenses/results-expenses.component';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptor/jwt.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SidebarComponent,
    FooterComponent,
    DashboardComponent,
    CustomersListComponent,
    CustomerRegisterComponent,
    CustomerUpdateComponent,
    CustomerDetailsComponent,
    JobRegisterComponent,
    JobUpdateComponent,
    ResultsJobsComponent,
    ExpenseRegisterComponent,
    ExpensesListComponent,
    UpdateExpenseComponent,
    ResultsExpensesComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RouterModule,
    CommonModule,
  ],
  providers: [  {
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent],

})
export class AppModule { }
