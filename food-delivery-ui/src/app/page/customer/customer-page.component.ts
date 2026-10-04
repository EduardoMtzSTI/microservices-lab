import { Component } from '@angular/core';
import { CustomerFormComponent } from '../../components/customer-form/customer-form.component';

@Component({
  imports: [CustomerFormComponent],
  selector: 'app-customer-page.component',
  templateUrl: './customer-page.component.html',
})
export class CustomerPageComponent {}
