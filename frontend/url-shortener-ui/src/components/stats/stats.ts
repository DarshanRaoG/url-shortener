import { HttpClient } from '@angular/common/http';
import { Component, inject, input, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UrlService } from '../../services/url-service';
import { UrlStats } from '../../models/url-stats.model';

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
  stats = signal<UrlStats | null>(null);

  createdDateString = '';
  expiryDateString = '';

  handleStatsClick() {
    const code = this.shortUrl().split('/').pop();
    if (!code) return;

    this.loading.set(true);

    this.urlService.getStats(code).subscribe({
      next: (result: UrlStats) => {
        this.stats.set(result);

        this.createdDateString = new Date(this.stats()!.createdAt).toUTCString();
        this.expiryDateString = new Date(this.stats()!.expiresAt).toUTCString();

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
