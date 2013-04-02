package karstenroethig.db.test.validation;

public class ValidationMessage {

    private String message;

    private Throwable throwable;

    public ValidationMessage( String message, Throwable throwable ) {
        this.message = message;
        this.throwable = throwable;
    }

    public ValidationMessage( String message ) {
        this( message, null );
    }

    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return throwable;
    }

}
