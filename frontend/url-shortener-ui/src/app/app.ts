import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavBar } from '../components/nav-bar/nav-bar';
import { Notification } from '../components/notification/notification';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavBar, Notification],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('url-shortener-ui');
}
