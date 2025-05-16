package roomescape.exception;

public class DatabaseForeignKeyException extends RuntimeException {

    public DatabaseForeignKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
