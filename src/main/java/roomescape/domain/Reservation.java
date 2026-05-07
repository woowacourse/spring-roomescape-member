package roomescape.domain;

import java.time.LocalDate;

public record Reservation(Long id, String name, LocalDate date, TimeSlot timeSlot, Theme theme) {

    public Reservation {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수이며 비어있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        if (timeSlot == null) {
            throw new IllegalArgumentException("유효하지 않은 예약 시간대입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("유효하지 않은 테마입니다.");
        }
    }

    public static Reservation transientOf(String name, LocalDate date, TimeSlot timeSlot, Theme theme) {
        return new Reservation(
                null,
                name,
                date,
                timeSlot,
                theme
        );
    }
}
