package roomescape.holiday.exception;

public class HolidayNotFoundException extends RuntimeException {
    private final Long id;

    public HolidayNotFoundException(Long id) {
        super("휴일이 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
