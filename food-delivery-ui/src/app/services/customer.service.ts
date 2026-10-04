import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer, NewCustomer } from '../models/Customer';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly customerUrl = 'http://localhost:8081/api/customers';

  createCustomer(customer: NewCustomer): Observable<Customer> {
    return this.http.post<Customer>(this.customerUrl, customer);
  }

  getCustomerById(id: String): Observable<Customer> {
    return this.http.get<Customer>(this.customerUrl + '/' + id);
  }

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.customerUrl);
  }
}
