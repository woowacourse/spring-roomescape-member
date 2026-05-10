package roomescape.reservation.dto;

import roomescape.reservation.model.Reservation;

import java.time.LocalDateTime;

public class ReservationResponse {

    private final Long reservationId;
    private final Long userId;
    private final String userName;
    private final Long scheduleId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final Long themeId;
    private final String themeName;

    private ReservationResponse(Long reservationId, Long userId, String userName, Long scheduleId,
                                LocalDateTime startAt, LocalDateTime endAt, Long themeId, String themeName) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.userName = userName;
        this.scheduleId = scheduleId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.themeId = themeId;
        this.themeName = themeName;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getUser().getName(),
                reservation.getSchedule().getId(),
                reservation.getSchedule().getStartAt(),
                reservation.getSchedule().getEndAt(),
                reservation.getSchedule().getTheme().getId(),
                reservation.getSchedule().getTheme().getName()
        );
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getThemeName() {
        return themeName;
    }
}
