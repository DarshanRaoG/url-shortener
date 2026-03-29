import { ChangeDetectionStrategy, Component, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Home {
  inputUrl = model<string>('');
  shortUrl = signal<string | null>(null);

  handleShortenClick() {
    this.shortUrl.set(this.inputUrl());
  }
}
