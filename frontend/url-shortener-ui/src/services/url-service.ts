import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UrlService {
  private http = inject(HttpClient);

  shortenUrl(url: string): Observable<string> {
    return this.http.post(`/api/shorten?url=${url}`, {}, { responseType: 'text' });
  }

  getStats(shortCode: string): Observable<object> {
    return this.http.get(`/api/stats/${shortCode}`);
  }
}
