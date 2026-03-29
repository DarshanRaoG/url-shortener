import { HttpClient } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, inject, model, signal } from '@angular/core';
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

  http = inject(HttpClient);

  handleShortenClick() {
    const url = this.inputUrl();
    if (!url) return;

    this.shortUrl.set(null); // clearing old result

    this.http.post(`/api/shorten?url=${url}`, {}, { responseType: 'text' }).subscribe((result) => {
      this.shortUrl.set(result);
    });
  }

  handleCopyClick() {
    const url = this.shortUrl();
    if (!url) return;

    navigator.clipboard
      .writeText(url)
      .then(() => alert('Copied!'))
      .catch(() => alert('Error while copying url!'));
  }
}
