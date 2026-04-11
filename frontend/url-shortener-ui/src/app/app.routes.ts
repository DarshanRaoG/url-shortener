import { Routes } from '@angular/router';
import { Home } from '../components/home/home';
import { Stats } from '../components/stats/stats';
import { About } from '../components/about/about';

export const routes: Routes = [
  {
    path: '',
    component: Home,
  },
  {
    path: 'stats',
    component: Stats,
  },
  {
    path: 'about',
    component: About,
  },
  {
    path: '**',
    redirectTo: '',
  },
];
