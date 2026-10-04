import { Component, effect, inject, input } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Product } from '../../models/Product';
import { NewOrder, OrderItem } from '../../models/Order';
import { MessageDialogComponent } from '../message-dialog/message-dialog.component';
import { MatAnchor, MatButtonModule } from '@angular/material/button';

@Component({
  templateUrl: 'cart.component.html',
  selector: 'cart',
  imports: [MatAnchor, MatButtonModule],
})
export class CartComponent {
  private readonly cartService = inject(CartService);
  private readonly orderService = inject(OrderService);
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);

  readonly cart = this.cartService.cart;
  readonly customerId = this.cartService.customerId;
  readonly restaurantId = this.cartService.restaurantId;

  // constructor() {
  //   effect(() => {
  //     console.log('Cart cambió:', this.cart());
  //   });
  // }

  removeFromCart = (product: Product) => {
    this.cartService.remove(product);
  };

  sendOrder() {
    const restaurantId = this.restaurantId();
    const customerId = this.customerId();

    if (!customerId || !restaurantId) return;

    const newOrder: NewOrder = {
      customerId: customerId,
      restaurantId: restaurantId,
      items: this.cartService.getOrderItems(),
    };

    this.orderService.create(newOrder).subscribe({
      next: () => {
        this.cartService.clear();
        this.showSnackBarMessage('Orden creada');
      },
    });
  }

  showMessageCustomerRequiered() {
    this.showDialogMessage(
      'Selecciona cliente',
      'Selecciona un cliente para poder agregar productos al carrito',
    );
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
