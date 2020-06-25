package org.jenkinsci.plugins.cloudhubdeployer.exception;

/**
 * CloudHubRequestException
 *
 * @author Vikas Chaudhary
 * @version 1.0.0
 */
public class CloudHubRequestException extends Exception {

    private static final long serialVersionUID = 2161055784155502631L;

    public CloudHubRequestException() {
        super();
    }

    public CloudHubRequestException(String message) {
        super(message);
    }

    public CloudHubRequestException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public CloudHubRequestException(Throwable rootCause) {
        super(rootCause);
    }
}

