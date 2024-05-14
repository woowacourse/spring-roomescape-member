package roomescape.reservation.controller;

import roomescape.reservation.domain.Reservation;
import java.time.LocalDate;

public class ReservationFilter {

    private final Long themeId;
    private final Long memberId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public ReservationFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        this.themeId = themeId;
        this.memberId = memberId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public boolean apply(Reservation reservation) {
        return checkThemeCondition(reservation) && checkMemberCondition(reservation)
                && checkDateIsAfterDateFrom(reservation) && checkDateIsBeforeDateTo(reservation);
    }

    private boolean checkThemeCondition(Reservation reservation) {
        if (themeId == null) {
            return true;
        }
        return reservation.getTheme().getId().equals(themeId);
    }

    private boolean checkMemberCondition(Reservation reservation) {
        if (memberId == null) {
            return true;
        }
        return reservation.getMember().getId().equals(memberId);
    }

    private boolean checkDateIsAfterDateFrom(Reservation reservation) {
        if (dateFrom == null) {
            return true;
        }
        return !reservation.getDate().isBefore(dateFrom);
    }

    private boolean checkDateIsBeforeDateTo(Reservation reservation) {
        if (dateTo == null) {
            return true;
        }
        return !reservation.getDate().isAfter(dateTo);
    }
}
