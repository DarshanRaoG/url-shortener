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
    const rawUrl = this.inputUrl();
    if (!rawUrl) return;

    const url = this.normalizeUrl(rawUrl);

    if (!url) {
      this.notificationService.showError('Please enter a valid URL!');
      return;
    }

    this.shortUrl.set(null);
    this.loading.set(true);

    this.urlService.shortenUrl(url).subscribe({
      next: (result) => {
        this.shortUrl.set(result);
        this.loading.set(false);
      },
      error: (err) => {
        console.log(err);
        this.notificationService.showError('Error while shortening URL!');
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

  private normalizeUrl(url: string): string | null {
    let finalUrl = url.trim();

    // Add https:// if missing
    if (!finalUrl.startsWith('http://') && !finalUrl.startsWith('https://')) {
      finalUrl = 'https://' + finalUrl;
    }

    // Validate URL
    try {
      new URL(finalUrl);
      return finalUrl;
    } catch {
      return null;
    }
  }
}
