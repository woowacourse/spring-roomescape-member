package roomescape.exception;

public class DeletionNotAllowedException extends RuntimeException {
    public DeletionNotAllowedException() {
        super("예약이 존재하는 시간은 삭제할 수 없습니다.");
    }
}
