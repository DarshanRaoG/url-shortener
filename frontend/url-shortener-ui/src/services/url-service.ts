import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UrlStats } from '../models/url-stats.model';

@Injectable({
  providedIn: 'root',
})
export class UrlService {
  private http = inject(HttpClient);

  shortenUrl(url: string): Observable<string> {
    return this.http.post(`/api/shorten?url=${url}`, {}, { responseType: 'text' });
  }

  getStats(shortCode: string): Observable<UrlStats> {
    return this.http.get<UrlStats>(`/api/stats/${shortCode}`);
  }
}
