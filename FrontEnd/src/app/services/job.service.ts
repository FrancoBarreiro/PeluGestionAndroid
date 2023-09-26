import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Job } from '../models/job';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  private URL = "http://localhost:8080/jobs";

  constructor(private httpClient: HttpClient) { }

  getJobById(id: Number): Observable<any> {
    return this.httpClient.get(this.URL + "/" + id);
  }

  saveJob(job: Job): Observable<any> {
    return this.httpClient.post(this.URL, job);
  }

  updateJob(job: Job): Observable<any> {
    return this.httpClient.put(this.URL, job);
  }

  deleteJob(id: Number): Observable<any> {
    return this.httpClient.delete(this.URL + '/' + id);
  }

  getJobsOrderByDate(page: Number, size: Number, sort: String): Observable<any> {
    return this.httpClient.get(this.URL + '/view?page=' + page + '/&size=' + size + '%by=' + sort);
  }

  getJobsFromDateToDate(page: Number, size: Number, from: string, to: string): Observable<any> {
    return this.httpClient.get(this.URL + '/results?page=' + page + '&size=' + size + '&from=' + from + '&to=' + to);
  }

  getSumTotalByPeriod(from:string, to:string): Observable<any>{
    return this.httpClient.get(this.URL+'/totalByPeriod?from='+from+'&to='+to)
  }

  getJobsByCustomerId(size:number,page:number, id:Number): Observable<any>{
    return this.httpClient.get(this.URL+'/jobs-customer/'+id+'?page='+page+'&size='+size)
  }
}
