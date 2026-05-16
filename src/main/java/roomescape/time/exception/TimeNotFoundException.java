package roomescape.time.exception;

public class TimeNotFoundException extends RuntimeException {
    private final Long id;

    public TimeNotFoundException(Long id) {
        super("해당 시간에 예약이 존재하여 삭제할 수 없습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
