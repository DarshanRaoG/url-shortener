import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Home } from '../components/home/home';
import { Stats } from '../components/stats/stats';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Home, Stats],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('url-shortener-ui');
}
