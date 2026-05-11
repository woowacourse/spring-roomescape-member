package roomescape.closeddate.domain;

import java.time.LocalDate;

public class ClosedDate {
    private Long id;
    private LocalDate date;

    private ClosedDate(Long id, LocalDate date) {
        validateDate(date);
        this.id = id;
        this.date = date;
    }

    public static ClosedDate create(LocalDate date) {
        ClosedDate reservationDate = new ClosedDate(null, date);
        validatePast(date);
        return reservationDate;
    }

    public static ClosedDate load(Long id, LocalDate date) {
        validateId(id);
        return new ClosedDate(id, date);
    }

    public Long id() {
        return id;
    }

    public LocalDate date() {
        return date;
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("휴일 날짜 ID는 필수입니다.");
        }
    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("휴일 날짜는 필수입니다.");
        }
    }

    private static void validatePast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("과거 날짜는 등록할 수 없습니다.");
        }
    }
}
