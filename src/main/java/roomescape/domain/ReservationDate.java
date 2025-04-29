package roomescape.domain;

import java.time.LocalDate;

public class ReservationDate {

    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        this.date = date;
    }

    public ReservationDate(LocalDate date, LocalDate currentDate) {
        validateDate(date, currentDate);
        this.date = date;
    }

    private void validateDate(LocalDate date, LocalDate currentDate) {
        if (date.isBefore(currentDate)) {
            throw new IllegalArgumentException("현재 날짜 기준으로 과거의 날짜는 예약할 수 없습니다.");
        }
    }

    public LocalDate getDate() {
        return date;
    }
}
