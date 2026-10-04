import { Component, inject, input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'dialog-message',
  templateUrl: 'message-dialog.component.html',
  imports: [MatButtonModule, MatDialogModule],
})
export class MessageDialogComponent {
  readonly data = inject<{ title: string; message: string }>(MAT_DIALOG_DATA);
}
