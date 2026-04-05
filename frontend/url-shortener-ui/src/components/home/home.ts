import { ChangeDetectionStrategy, Component, inject, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UrlService } from '../../services/url-service';
import { NotificationService } from '../../services/notification-service';

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
  loading = signal(false);

  urlService = inject(UrlService);
  notificationService = inject(NotificationService);

  handleShortenClick() {
    const url = this.inputUrl();
    if (!url) return;

    this.shortUrl.set(null); // clearing old result
    this.loading.set(true);

    this.urlService.shortenUrl(url).subscribe({
      next: (result) => {
        this.shortUrl.set(result);
        this.loading.set(false);
      },
      error: (err) => {
        console.log(err);
        this.notificationService.showError('Error while shortening URL! ');
        this.loading.set(false);
      },
    });
  }

  handleCopyClick() {
    const url = this.shortUrl();
    if (!url) return;

    navigator.clipboard
      .writeText(url)
      .then(() => this.notificationService.showSuccess('Copied!'))
      .catch(() => this.notificationService.showError('Error while copying url!'));
  }
}
