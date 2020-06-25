package org.jenkinsci.plugins.cloudhubdeployer.exception;

/**
 * ValidationException
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = 2161055784155502757L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public ValidationException(Throwable rootCause) {
        super(rootCause);
    }
}
