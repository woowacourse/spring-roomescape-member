package roomescape.domain.reservation;

import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

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
        validateDate(date);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_NAME);
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_DATE);
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
