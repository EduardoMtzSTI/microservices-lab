import { Component, inject, output, signal } from '@angular/core';
import { MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateRestaurant, Restaurant } from '../../models/Restaurant';
import { RestaurantService } from '../../services/restaura.service';

@Component({
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatError,
  ],
  selector: 'restaurant-form',
  templateUrl: './restaurant-form.component.html',
})
export class RestaurantFormComponent {
  private readonly restauranService = inject(RestaurantService);
  private readonly fb = inject(FormBuilder);

  restaurantCreated = output<Restaurant>();

  restaurantForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.min(3)]],
    address: ['', [Validators.required, Validators.min(10)]],
    phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
    openingTime: ['', Validators.required],
    closingTime: ['', Validators.required],
  });

  onSubmit(): void {
    if (this.restaurantForm.invalid) {
      this.restaurantForm.markAllAsTouched();
      return;
    }

    const restaurant: CreateRestaurant = this.restaurantForm.getRawValue();

    this.restauranService.createRestaurant(restaurant).subscribe({
      next: (data) => {
        this.restaurantCreated.emit(data);
        this.restaurantForm.reset();
        this.restaurantForm.markAsUntouched();
      },
    });
  }
}
