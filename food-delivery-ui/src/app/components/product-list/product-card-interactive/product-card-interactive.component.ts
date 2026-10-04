import { Component, input } from '@angular/core';
import { Product } from '../../../models/Product';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'product-card-interactive',
  imports: [MatButtonModule],
  templateUrl: 'product-card-interactive.component.html',
})
export class ProductCardInteractiveComponent {
  product = input.required<Product>();
  onAdd = input.required<(product: Product) => void>();

  onClickAdd() {
    this.onAdd()(this.product());
  }
}
