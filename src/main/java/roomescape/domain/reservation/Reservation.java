package roomescape.domain.reservation;

import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.common.exception.BusinessException;
import roomescape.common.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validateName(String name) {
        if (!name.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new BusinessException(ErrorCode.RESERVATION_NAME_INVALID);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
