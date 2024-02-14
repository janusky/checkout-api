package checkoutapi.security.payload;

import java.util.Date;

import org.springframework.http.HttpStatus;

/**
 * Error model for interacting with client.
 *
 */
public class ErrorResponse {
	/** HTTP Response Status Code. */
    private final HttpStatus status;

    /** General Error message. */
    private final String message;

	/** Identificador del error. */
    private final String errorId;

    private final Date timestamp;

    protected ErrorResponse(final String message, final String errorId, HttpStatus status) {
        this.message = message;
        this.errorId = errorId;
        this.status = status;
        this.timestamp = new java.util.Date();
    }

    public static ErrorResponse of(final String message, final String errorId, HttpStatus status) {
        return new ErrorResponse(message, errorId, status);
    }

    public Integer getStatus() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public String getErrorId() {
        return errorId;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
