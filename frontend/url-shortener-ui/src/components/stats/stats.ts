import { HttpClient } from '@angular/common/http';
import { Component, inject, input, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UrlService } from '../../services/url-service';

@Component({
  selector: 'app-stats',
  imports: [FormsModule],
  templateUrl: './stats.html',
  styleUrl: './stats.css',
})
export class Stats {
  urlService = inject(UrlService);
  shortUrl = model('');
  loading = signal(false);
  stats = signal<any | null>(null);

  originalURL = signal('');
  clicks = signal('');
  createdAt = signal(new Date());
  expiresAt = signal(new Date());

  handleStatsClick() {
    const code = this.shortUrl().split('/').pop();
    if (!code) return;

    this.loading.set(true);

    this.urlService.getStats(code).subscribe({
      next: (result) => {
        this.stats.set(result);

        this.originalURL.set(this.stats().originalUrl);
        this.clicks.set(this.stats().clickCount);
        this.createdAt.set(new Date(this.stats().createdAt));
        this.expiresAt.set(new Date(this.stats().expiresAt));

        this.loading.set(false);
      },
      error: (err) => {
        console.log(err);
        alert('Error fetching statistics for this URL! ');
        this.loading.set(false);
      },
    });
  }
}
