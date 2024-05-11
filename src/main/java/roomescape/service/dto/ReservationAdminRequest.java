package roomescape.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationAdminRequest {

    private final String date;
    private final Long themeId;
    private final Long timeId;
    private final Long memberId;

    public ReservationAdminRequest(String date, Long themeId, Long timeId, Long memberId) {
        validateDateExist(date);
        validateDateFormat(date);
        validateTimeIdExist(timeId);
        validateIdNaturalNumber(timeId);
        validateThemeIdExist(themeId);
        validateIdNaturalNumber(themeId);
        validateMemberIdExist(memberId);
        validateIdNaturalNumber(memberId);
        this.date = date;
        this.themeId = themeId;
        this.timeId = timeId;
        this.memberId = memberId;
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

    private void validateIdNaturalNumber(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("아이디는 자연수여야 합니다.");
        }
    }

    private void validateThemeIdExist(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마 아이디는 반드시 입력되어야 합니다.");
        }
    }

    private void validateMemberIdExist(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("멤버 아이디는 반드시 입력되어야 합니다.");
        }
    }

    public Reservation toReservation() {
        return new Reservation(
                null,
                new Member(memberId, (Name) null, null, null, null),
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

    public Long getMemberId() {
        return memberId;
    }
}
