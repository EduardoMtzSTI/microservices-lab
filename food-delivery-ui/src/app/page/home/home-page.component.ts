import { Component, inject } from '@angular/core';
import { CustomerCardComponent } from '../../components/customer-card/customer-card.component';
import { Customer } from '../../models/Customer';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { CartService } from '../../services/cart.service';
import { ProductCatalogComponent } from '../../components/product-catalog/product-catalog.component';
import { CartComponent } from '../../components/cart/cart.component';

@Component({
  imports: [
    CustomerCardComponent,
    MatButtonModule,
    MatButtonModule,
    MatDialogModule,
    ProductCatalogComponent,
    CartComponent,
  ],
  selector: 'app-home-page.component',
  templateUrl: './home-page.component.html',
})
export class HomePageComponent {
  private readonly cartService = inject(CartService);
  readonly customerId = this.cartService.customerId;

  setCustomer(customer: Customer) {
    this.customerId.set(customer.id);
  }
}
