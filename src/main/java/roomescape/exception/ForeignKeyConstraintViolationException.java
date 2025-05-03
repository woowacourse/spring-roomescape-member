package roomescape.exception;

public class ForeignKeyConstraintViolationException extends RuntimeException {

    private static final String message = "[ERROR] 해당 데이터를 사용중이기 때문에 삭제할 수 없습니다.";

    public ForeignKeyConstraintViolationException() {
        super(message);
    }
}
