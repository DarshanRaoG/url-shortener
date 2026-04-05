import { Injectable, signal } from '@angular/core';
import { NotificationObject } from '../models/notification.model';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  notifications = signal<NotificationObject[]>([]);

  showSuccess(notifyMessage: string) {
    this.show(notifyMessage, 'success');
  }

  showError(notifyMessage: string) {
    this.show(notifyMessage, 'error');
  }

  showInfo(notifyMessage: string) {
    this.show(notifyMessage, 'info');
  }

  show(notifyMessage: string, notifyType: NotificationObject['type']) {
    const newId = Date.now();
    const newNotification: NotificationObject = {
      id: newId,
      type: notifyType,
      message: notifyMessage,
    };
    this.notifications.update((prevNotifications) => [...prevNotifications, newNotification]);

    //Auto remove notification after 3 seconds
    setTimeout(() => this.removeNotification(newId), 3000);
  }

  removeNotification(id: number) {
    this.notifications.update((curr) => curr.filter((notification) => notification.id !== id));
  }
}
