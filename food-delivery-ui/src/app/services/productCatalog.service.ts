import { inject, Injectable } from '@angular/core';
import { ProductCatalog } from '../models/ProductCatalog';
import { Menu, ProductMenu, RestaurantMenu } from '../models/Menu';
import { map, Observable } from 'rxjs';
import { ProductSerivce } from './product.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class CatalogService {
  private readonly http = inject(HttpClient);
  private readonly productsUrl = 'http://localhost:8082/api/products';

  getCatalog(): Observable<Menu[]> {
    return this.http
      .get<ProductCatalog[]>(this.productsUrl + '/catalog')
      .pipe(map((catalog) => this.mapToMenu(catalog)));
  }

  mapToMenu(catalog: ProductCatalog[]): Menu[] {
    if (catalog.length === 0) return [];

    //Extracion de restaurantes no repetidos
    const restaurants = this.getDistinctRestaurants(catalog);

    //Regresa el menu de cada restaurant
    return restaurants.map((restaurant) => ({
      restaurant: restaurant,
      products: this.getProductsByRestaurantId(catalog, restaurant.id),
    }));
  }

  getDistinctRestaurants(catalog: ProductCatalog[]): RestaurantMenu[] {
    return catalog.reduce<RestaurantMenu[]>((restaurants, currentProduct) => {
      const exists = this.existsRestaurant(restaurants, currentProduct);
      if (exists) {
        return restaurants;
      } else {
        return [...restaurants, { ...currentProduct.restaurant }];
      }
    }, []);
  }

  getProductsByRestaurantId(catalog: ProductCatalog[], restaurantId: string) {
    return catalog
      .filter((product) => product.restaurant?.id === restaurantId)
      .map((product) => {
        const productMenu: ProductMenu = this.mapToProductMenu(product);
        return productMenu;
      });
  }

  existsRestaurant(restaurants: RestaurantMenu[], currentProduct: ProductCatalog) {
    return restaurants.some((restaurant) => restaurant?.id === currentProduct.restaurant.id);
  }

  mapToProductMenu(product: ProductCatalog): ProductMenu {
    return {
      id: product.id,
      restaurantId: product.restaurant.id,
      name: product.name,
      price: product.price,
      currency: product.currency,
      description: product.description,
      category: product.category,
      available: product.available,
    };
  }
}
