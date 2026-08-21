import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'huf',
  standalone: true
})
export class HufCurrencyPipe implements PipeTransform {
  transform(value: number | null | undefined, showSign: boolean = false): string {
    if (value === null || value === undefined || isNaN(value)) {
      return '0 Ft';
    }

    const rounded = Math.round(value);
    const formatted = rounded.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ');

    if (showSign && rounded > 0) {
      return `+${formatted} Ft`;
    }

    return `${formatted} Ft`;
  }
}
