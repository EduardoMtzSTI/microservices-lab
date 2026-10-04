import { Component, inject, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { Customer, NewCustomer } from '../../models/Customer';

@Component({
  imports: [ReactiveFormsModule],
  standalone: true,
  selector: 'customer-form',
  templateUrl: './customer-form.component.html',
})
export class CustomerFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly customerService = inject(CustomerService);

  customerCreated = output<Customer>();

  customerForm = this.fb.nonNullable.group({
    fullName: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    number: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
    address: ['', [Validators.required, Validators.minLength(5)]],
  });

  onSubmit(): void {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    const customerData: NewCustomer = this.customerForm.getRawValue();

    this.customerService.createCustomer(customerData).subscribe({
      next: (customer) => {
        this.customerCreated.emit(customer);
        this.customerForm.reset();
        this.customerForm.markAsUntouched();
      },
    });
  }
}
