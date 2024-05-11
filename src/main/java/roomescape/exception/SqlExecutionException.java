package roomescape.exception;

public class SqlExecutionException extends BadRequestException{

    public SqlExecutionException() {}

    public SqlExecutionException(String message) {
        super(message);
    }
}
