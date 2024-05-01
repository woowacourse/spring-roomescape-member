package roomescape.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;

public class ReservationRequestDto {

    private final String name;
    private final String date;
    private final Long timeId;

    public ReservationRequestDto(String name, String date, Long timeId) {
        validateNameExist(name);
        validateDateExist(date);
        validateDateFormat(date);
        validateTimeIdExist(timeId);
        validateTimeIdNaturalNumber(timeId);
        this.name = name;
        this.date = date;
        this.timeId = timeId;
    }

    private void validateNameExist(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 반드시 입력되어야 합니다.");
        }
    }

    private void validateDateExist(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("날짜는 반드시 입력되어야 합니다.");
        }
    }

    private void validateDateFormat(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다.");
        }
    }

    private void validateTimeIdExist(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("시간 아이디는 반드시 입력되어야 합니다.");
        }
    }

    private void validateTimeIdNaturalNumber(Long timeId) {
        if (timeId <= 0) {
            throw new IllegalArgumentException("시간 아이디는 자연수여야 합니다.");
        }
    }

    public Reservation toReservation() {
        return new Reservation(null, new Name(name), new ReservationDate(date), new ReservationTime(timeId,
                (LocalTime) null));
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public long getTimeId() {
        return timeId;
    }
}
