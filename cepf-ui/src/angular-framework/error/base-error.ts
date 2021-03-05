// export class BaseError extends HttpErrorResponse {
//   public timestamp: Date;
//   public path: string;
// }

export class BaseError {
  public timestamp: Date;
  public status: number;
  public error: any;
  public message: string;
  public path: string;

  constructor(status?: number, message?: string) {
    this.status = status;
    this.message = message;
  }
}
