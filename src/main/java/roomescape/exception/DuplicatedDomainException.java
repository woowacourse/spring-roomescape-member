package roomescape.exception;

public class DuplicatedDomainException extends RuntimeException {

    public DuplicatedDomainException() {
        super("이미 존재하는 데이터는 추가할 수 없습니다.");
    }
}
