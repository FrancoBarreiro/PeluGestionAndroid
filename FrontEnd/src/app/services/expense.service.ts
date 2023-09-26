import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Expense } from '../models/expense';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {

  private URL = "http://localhost:8080/expenses";

  constructor(private httpClient:HttpClient) { }

  saveExpense(expense:Expense):Observable<any>{
    return this.httpClient.post(this.URL,expense);
  }

  getExpenses(page:number, size:number):Observable<any>{
    return this.httpClient.get(this.URL+'/paged?page='+page+'&size='+size);
  }

  getExpenseById(id:Number):Observable<any>{
    return this.httpClient.get(this.URL+'/'+id);
  }
  
  updateExpense(expense:Expense):Observable<any>{
    return this.httpClient.put(this.URL,expense);
  }

  deleteExpense(id:Number):Observable<any>{
    return this.httpClient.delete(this.URL+'/'+id);
  }

  getExpensesFromDateToDate(page: Number, size: Number, from: string, to: string): Observable<any> {
    return this.httpClient.get(this.URL + '/results?page=' + page + '&size=' + size + '&from=' + from + '&to=' + to);
  }

  getSumTotalByPeriod(from:string, to:string): Observable<any>{
    return this.httpClient.get(this.URL+'/totalByPeriod?from='+from+'&to='+to)
  }

}
