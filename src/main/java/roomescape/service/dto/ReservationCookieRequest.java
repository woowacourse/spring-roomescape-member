package roomescape.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationCookieRequest {

    private final String date;
    private final Long themeId;
    private final Long timeId;

    public ReservationCookieRequest(String date, Long themeId, Long timeId) {
        validateDateExist(date);
        validateDateFormat(date);
        validateThemeIdExist(themeId);
        validateIdNaturalNumber(themeId);
        validateTimeIdExist(timeId);
        validateIdNaturalNumber(timeId);
        this.themeId = themeId;
        this.date = date;
        this.timeId = timeId;
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

    private void validateThemeIdExist(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마 아이디는 반드시 입력되어야 합니다.");
        }
    }

    private void validateIdNaturalNumber(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("아이디는 자연수여야 합니다.");
        }
    }

    private void validateTimeIdExist(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("시간 아이디는 반드시 입력되어야 합니다.");
        }
    }

    public Reservation toReservation(Member member) {
        return new Reservation(
                null,
                member,
                new Theme(themeId, null, null, null),
                new ReservationDate(date),
                new ReservationTime(timeId, (LocalTime) null));
    }

    public String getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getTimeId() {
        return timeId;
    }
}
