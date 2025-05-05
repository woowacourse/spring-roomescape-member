package roomescape.exception;

public class DuplicateTimeException extends DuplicateEntityException {

    public DuplicateTimeException() {
        super("이미 존재하는 시간이다.");
    }
}
