package roomescape.reservation.dto;

import roomescape.reservation.model.Reservation;

import java.time.LocalDateTime;

public class ReservationResponse {

    private final Long reservationId;
    private final Long userId;
    private final String userName;
    private final Long themeId;
    private final String themeName;
    private final Long scheduleId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    private ReservationResponse(Long reservationId, Long userId, String userName, Long themeId, String themeName, Long scheduleId, LocalDateTime startAt, LocalDateTime endAt) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.userName = userName;
        this.themeId = themeId;
        this.themeName = themeName;
        this.scheduleId = scheduleId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getUser().getName(),
                reservation.getTheme().getId(),
                reservation.getTheme().getName(),
                reservation.getSchedule().getId(),
                reservation.getSchedule().getStartAt(),
                reservation.getSchedule().getEndAt()
        );
    }
}
