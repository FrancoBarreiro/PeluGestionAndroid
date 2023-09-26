import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CustomerUpdateService {

  public customerRegistered: EventEmitter<void> = new EventEmitter<void>();
  
  constructor() { }
}
