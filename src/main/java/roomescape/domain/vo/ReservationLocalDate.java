package roomescape.domain.vo;

import java.time.LocalDate;

public record ReservationLocalDate(
    LocalDate value
) {

    public static ReservationLocalDate createForSave(LocalDate date) {
        validateAfterToday(date);

        return new ReservationLocalDate(date);
    }

    private static void validateAfterToday(LocalDate date) {
        if (!date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("오늘 이후의 날짜만 선택할 수 있습니다.");
        }
    }
}
