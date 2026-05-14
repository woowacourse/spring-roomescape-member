package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.global.exception.validation.InvalidIdException;
import roomescape.global.exception.validation.InvalidNameException;
import roomescape.global.exception.validation.InvalidNameLengthException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation {

    private static final int MAX_NAME_LENGTH = 50;

    @EqualsAndHashCode.Include
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final boolean cancel;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, boolean cancel, ReservationTime time, Theme theme) {
        validateId(id);
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.cancel = cancel;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this(id, name, date, false, time, theme);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException();
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidNameLengthException("이름의 길이는 " + MAX_NAME_LENGTH + "를 넘을 수 없습니다.");
        }
    }

    private void validateId(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new InvalidIdException(id);
        }
    }

    public LocalDateTime getReservationDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }

    public boolean isFutureOrPresent(LocalDateTime compareDateTime) {
        return !getReservationDateTime().isBefore(compareDateTime);
    }
}
