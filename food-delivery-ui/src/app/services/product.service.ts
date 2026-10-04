import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { CreateProduct, Product } from '../models/Product';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductSerivce {
  private readonly http = inject(HttpClient);
  private readonly productsUrl = 'http://localhost:8082/api/products';

  createProduct(product: CreateProduct): Observable<Product> {
    return this.http.post<Product>(this.productsUrl, product);
  }

  getProductsByRestaurantId(id: string): Observable<Product[]> {
    return this.http.get<Product[]>(this.productsUrl + '/restaurant/' + id);
  }
}
