package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import roomescape.domain.reservationtime.ReservationTime;

public class ReservationDate {

    private final LocalDate value;

    private ReservationDate(LocalDate value) {
        this.value = value;
    }

    public static ReservationDate from(String value) {
        validateValue(value);
        LocalDate reservationDate = convertLocalDate(value);
        return new ReservationDate(reservationDate);
    }

    private static LocalDate convertLocalDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }

    private static void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("예약 날짜는 비어있을 수 없습니다.");
        }
    }

    public boolean isSame(LocalDateTime dateTime) {
        LocalDate today = LocalDate.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
        return isSame(today);
    }

    public boolean isBefore(LocalDateTime dateTime) {
        LocalDate today = LocalDate.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
        return isBefore(today);
    }

    public List<ReservationTime> filterPastTime(List<ReservationTime> reservationTimes, LocalDateTime dateTime) {
        if (isSame(dateTime)) {
            return reservationTimes.stream()
                    .filter(time -> !time.isBefore(dateTime))
                    .toList();
        }
        return reservationTimes;
    }

    private boolean isSame(LocalDate date) {
        return value.equals(date);
    }

    private boolean isBefore(LocalDate date) {
        return value.isBefore(date);
    }

    public String toStringDate() {
        return value.toString();
    }

    public LocalDate getValue() {
        return value;
    }
}
