import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'time',
  standalone: true,
})
export class TimePipe implements PipeTransform {
  transform(value: string): string {
    const [hours, minutes] = value.split(':');

    const h = Number(hours);
    const hour12 = h % 12 || 12;
    const period = h >= 12 ? 'PM' : 'AM';

    return `${hour12}:${minutes} ${period}`;
  }
}
