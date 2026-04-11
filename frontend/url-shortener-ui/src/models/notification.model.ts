export interface NotificationObject {
  id: number;
  type: 'success' | 'error' | 'info';
  message: string;
}
