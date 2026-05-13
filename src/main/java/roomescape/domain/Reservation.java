package roomescape.domain;

import roomescape.command.ReservationSaveCommand;
import roomescape.exception.BadRequestException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;
import java.util.Objects;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
    public Reservation {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private void validateTheme(Theme theme) {
        if (Objects.isNull(theme)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION_THEME);
        }
    }

    private void validateTime(ReservationTime time) {
        if (Objects.isNull(time)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION_NAME);
        }
        if (name.isBlank()) {
            throw new BadRequestException(ErrorCode.BLANK_RESERVATION_NAME);
        }
    }

    private void validateDate(LocalDate date) {
        if (Objects.isNull(date)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION_DATE);
        }
    }

    public static Reservation forSave(ReservationSaveCommand command, ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, command.name(), command.date(), reservationTime, theme);
    }

    public long timeId() {
        return time.id();
    }

    public long themeId() {
        return theme.id();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reservation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
