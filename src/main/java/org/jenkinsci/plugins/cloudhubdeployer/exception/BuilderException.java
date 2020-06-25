package org.jenkinsci.plugins.cloudhubdeployer.exception;

/**
 * BuilderException
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public class BuilderException extends Exception {

    private static final long serialVersionUID = 2219154714175103769L;

    public BuilderException() {
        super();
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public BuilderException(Throwable rootCause) {
        super(rootCause);
    }
}
