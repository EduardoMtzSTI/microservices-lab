import { Component, inject, input, output } from '@angular/core';
import { ProductSerivce } from '../../services/product.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateProduct, Product } from '../../models/Product';
import { ProductCategory } from '../../models/ProductCategory';
import { MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';

@Component({
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatSelectModule,
    MatError,
  ],
  selector: 'product-form',
  templateUrl: './product-form.component.html',
})
export class ProductFormComponent {
  private readonly productService = inject(ProductSerivce);
  private readonly fb = inject(FormBuilder);

  productCategories = Object.values(ProductCategory);
  productCreated = output<Product>();
  restaurantId = input.required<string>();

  productForm = this.fb.nonNullable.group({
    name: ['', [Validators.min(3)]],
    price: [''],
    description: [''],
    category: [ProductCategory.COMIDA, Validators.required],
  });

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const formData = this.productForm.getRawValue();
    if (formData.category === null) {
      return;
    }

    const productData: CreateProduct = {
      restaurantId: this.restaurantId(),
      name: formData.name,
      price: formData.price,
      description: formData.description,
      category: formData.category,
      currency: 'MXN',
    };

    this.productService.createProduct(productData).subscribe({
      next: (data) => {
        this.productCreated.emit(data);
        this.productForm.reset();
        this.productForm.markAsUntouched();
      },
    });
  }
}
