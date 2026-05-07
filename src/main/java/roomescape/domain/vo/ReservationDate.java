package roomescape.domain.vo;

import java.time.LocalDate;

public record ReservationDate(
        LocalDate value
) {

    public static ReservationDate createForSave(LocalDate date) {
        validateAfterToday(date);

        return new ReservationDate(date);
    }

    private static void validateAfterToday(LocalDate date) {
        if (!date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("오늘 이후의 날짜만 선택할 수 있습니다.");
        }
    }
}
