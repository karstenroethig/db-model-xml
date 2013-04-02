package karstenroethig.db.test.validation;

import java.util.ArrayList;
import java.util.List;


public class ValidationResult {

    private List<ValidationMessage> errors = new ArrayList<ValidationMessage>();

    private List<ValidationMessage> warnings = new ArrayList<ValidationMessage>();

    public void addError( String message, Throwable throwable ) {
        errors.add( new ValidationMessage( message, throwable ) );
    }

    public void addError( String message ) {
        addError( message, null );
    }

    public void addWarning( String message, Throwable throwable ) {
        warnings.add( new ValidationMessage( message, throwable ) );
    }

    public void addWarning( String message ) {
        addWarning( message, null );
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public List<ValidationMessage> getErrors() {
        return errors;
    }

    public List<ValidationMessage> getWarnings() {
        return warnings;
    }
}
