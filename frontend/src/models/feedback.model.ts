export type FeedbackType = 'success' | 'error' | null;

export interface FeedbackMessage {
  type: FeedbackType;
  message: string;
}
