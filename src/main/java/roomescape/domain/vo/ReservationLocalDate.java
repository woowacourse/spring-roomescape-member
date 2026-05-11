package roomescape.domain.vo;

import java.time.LocalDate;
import java.util.Objects;

public final class ReservationLocalDate {
    private final LocalDate value;

    public ReservationLocalDate(LocalDate value) {
        this.value = value;
    }

    public static ReservationLocalDate createForSave(LocalDate date) {
        validateAfterToday(date);

        return new ReservationLocalDate(date);
    }

    private static void validateAfterToday(LocalDate date) {
        if (!date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("오늘 이후의 날짜만 선택할 수 있습니다.");
        }
    }

    public LocalDate value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (ReservationLocalDate) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ReservationLocalDate[" +
            "value=" + value + ']';
    }

}
