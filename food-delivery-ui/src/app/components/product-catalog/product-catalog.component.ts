import { Component, inject, input, OnInit, signal } from '@angular/core';
import { ProductCatalog } from '../../models/ProductCatalog';
import { ProductCardInteractiveComponent } from '../product-list/product-card-interactive/product-card-interactive.component';
import { ProductListComponent } from '../product-list/product-list.component';
import { Menu } from '../../models/Menu';
import { CatalogService } from '../../services/productCatalog.service';
import { Product } from '../../models/Product';
import { CartService } from '../../services/cart.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { MessageDialogComponent } from '../message-dialog/message-dialog.component';
import { TimePipe } from '../../shared/pipes/time.pipe';

@Component({
  selector: 'product-catalog',
  templateUrl: 'product-catalog.component.html',
  imports: [ProductCardInteractiveComponent, ProductListComponent, TimePipe],
})
export class ProductCatalogComponent implements OnInit {
  private readonly catalogService = inject(CatalogService);
  private readonly cartService = inject(CartService);
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);

  readonly cart = this.cartService.cart;

  menus = signal<Menu[]>([]);

  ngOnInit() {
    this.catalogService.getCatalog().subscribe({
      next: (menus) => {
        this.menus.set(menus);
      },
    });
  }

  addToCart = (product: Product) => {
    const validated = this.cartService.validateAddToCart(product);
    if (!validated.isValid) {
      if (validated.messages.length > 0) this.showAddProductErrorsMessages(validated.messages);
      return;
    }
    this.cartService.add(product);
    const message = 'Producto (' + product.name + ')  agregado al carrito';
    this.showSnackBarMessage(message);
  };

  showAddProductErrorsMessages(messages: string[]) {
    const errorMessages: string = messages.reduce((messaages, msg) => msg + '.' + messaages, '');
    this.showDialogMessage('Error al agregar el producto', errorMessages);
  }

  showDialogMessage(title: string, message: string) {
    this.dialog.open(MessageDialogComponent, {
      data: {
        title,
        message,
      },
    });
  }

  showSnackBarMessage(message: string) {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000,
    });
  }
}
