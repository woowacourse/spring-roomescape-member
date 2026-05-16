package roomescape.closeddate.domain;

import java.time.LocalDate;
import roomescape.common.exception.DomainValidationException;

public class ClosedDate {
    private Long id;
    private LocalDate date;

    private ClosedDate(Long id, LocalDate date) {
        validateDate(date);
        this.id = id;
        this.date = date;
    }

    public static ClosedDate create(LocalDate date) {
        validatePast(date);
        return new ClosedDate(null, date);
    }

    public static ClosedDate load(Long id, LocalDate date) {
        return new ClosedDate(id, date);
    }

    public Long id() {
        return id;
    }

    public LocalDate date() {
        return date;
    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new DomainValidationException("휴일 날짜는 필수입니다.");
        }
    }

    private static void validatePast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("과거 날짜는 등록할 수 없습니다.");
        }
    }
}
