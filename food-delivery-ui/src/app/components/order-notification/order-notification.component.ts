import { Component, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarRef } from '@angular/material/snack-bar';

@Component({
  selector: 'order-notification',
  standalone: true,
  imports: [MatIconModule, MatButtonModule],
  templateUrl: 'order-notification.component.html',
})
export class OrderNotificationComponent {
  private readonly snackBarRef = inject(MatSnackBarRef<OrderNotificationComponent>);
  close(): void {
    this.snackBarRef.dismiss();
  }
}
