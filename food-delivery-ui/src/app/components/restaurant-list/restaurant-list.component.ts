import { Component, input, output } from '@angular/core';
import { Restaurant } from '../../models/Restaurant';

@Component({
  imports: [],
  selector: 'restaurant-list',
  templateUrl: './restaurant-list.component.html',
})
export class RestaurantListComponent {
  restaurants = input.required<Restaurant[]>();
  selectedRestaurant = output<Restaurant>();

  select(restaurant: Restaurant): void {
    this.selectedRestaurant.emit(restaurant);
  }
}
