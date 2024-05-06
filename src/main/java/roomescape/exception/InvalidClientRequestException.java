package roomescape.exception;

public class InvalidClientRequestException extends IllegalArgumentException {
    private final ErrorType errorType;
    private final String fieldName;
    private final String value;

    public InvalidClientRequestException(final ErrorType errorType, final String fieldName, final String value) {
        super();
        this.errorType = errorType;
        this.fieldName = fieldName;
        this.value = value;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }
}
