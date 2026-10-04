import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { Customer } from '../../models/Customer';
import { MatSelectModule } from '@angular/material/select';
import { CustomerService } from '../../services/customer.service';

@Component({
  imports: [MatSelectModule],
  selector: 'customer-card',
  templateUrl: './customer-card.component.html',
})
export class CustomerCardComponent implements OnInit {
  private readonly customerService = inject(CustomerService);

  customers = signal<Customer[]>([]);
  selectedCustomer = output<Customer>();
  currentCustomer = signal<Customer | null>(null);

  ngOnInit() {
    this.customerService.getAllCustomers().subscribe({
      next: (customers) => {
        this.customers.set(customers);
      },
    });
  }

  selectCustomer(customer: Customer) {
    this.selectedCustomer.emit(customer);
    this.currentCustomer.set(customer);
  }
}
