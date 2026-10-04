import { Component, inject, signal, OnInit, effect } from '@angular/core';
import { RestaurantFormComponent } from '../../components/restaurant-form/restaurant-form.component';
import { RestaurantListComponent } from '../../components/restaurant-list/restaurant-list.component';
import { Restaurant } from '../../models/Restaurant';
import { RestaurantService } from '../../services/restaura.service';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { Product } from '../../models/Product';
import { ProductListComponent } from '../../components/product-list/product-list.component';
import { ProductSerivce } from '../../services/product.service';
import { ProductFormComponent } from '../../components/product-form/product-form.component';
import { ViewChild } from '@angular/core';
import { MatDrawer } from '@angular/material/sidenav';
import { ProductCardComponent } from '../../components/product-list/product-card/product-card.component';
import { MatTabGroup, MatTab } from '@angular/material/tabs';
import { RestaurantDashboardComponent } from '../../components/restaurant-dashboard/restaurat-dashboard.component';

type DrawerContent = 'restaurante' | 'producto' | null;

@Component({
  imports: [
    RestaurantFormComponent,
    RestaurantListComponent,
    ProductFormComponent,
    ProductListComponent,
    MatSidenavModule,
    MatButtonModule,
    ProductCardComponent,
    MatTabGroup,
    MatTab,
    RestaurantDashboardComponent,
  ],
  selector: 'app-restaurant-page.component',
  templateUrl: './restaurant-page.component.html',
})
export class RestaurantPageComponent implements OnInit {
  private readonly restaurantService = inject(RestaurantService);
  private readonly productService = inject(ProductSerivce);

  restaurants = signal<Restaurant[]>([]);
  selectedRestaurant = signal<Restaurant | null>(null);
  products = signal<Product[]>([]);

  @ViewChild('drawer')
  drawer!: MatDrawer;
  drawerContent = signal<DrawerContent>(null);

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    this.restaurantService.getRestaurants().subscribe({
      next: (restaurant) => {
        this.restaurants.set(restaurant);
      },
    });
  }

  addRestaurant(newRestaurant: Restaurant) {
    this.restaurants.update((restaurants) => [...restaurants, newRestaurant]);
  }

  getProducts(): void {
    const restaurant = this.selectedRestaurant();

    if (!restaurant) {
      return;
    }

    this.productService.getProductsByRestaurantId(restaurant.id).subscribe({
      next: (products) => {
        this.products.set(products);
      },
    });
  }

  select(restaurant: Restaurant): void {
    this.selectedRestaurant.set(restaurant);
    this.getProducts();
  }

  openRestaurantForm(): void {
    this.drawerContent.set('restaurante');
    this.drawer.open();
  }

  openProductForm(): void {
    this.drawerContent.set('producto');
    this.drawer.open();
  }
}
