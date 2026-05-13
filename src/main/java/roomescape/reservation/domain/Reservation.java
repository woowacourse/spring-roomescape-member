package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateId(id);
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateName(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름의 길이는 " + MAX_NAME_LENGTH + "를 넘을 수 없습니다.");
        }
    }

    private void validateId(Long id) {
        if(Objects.isNull(id) || id <= 0) {
            throw new IllegalArgumentException("ID 비어있거나 음수일 수 없습니다.");
        }
    }

    public LocalDateTime getReservationDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }
}
