import { Component, input } from '@angular/core';
import { Product } from '../../../models/Product';

@Component({
  selector: 'product-card',
  imports: [],
  templateUrl: 'product-card.component.html',
})
export class ProductCardComponent {
  product = input.required<Product>();
}
