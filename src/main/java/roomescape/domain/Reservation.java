package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class Reservation {

    private final UUID id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(
            UUID id,
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme
    ) {
        validateId(id);
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("예약엔 식별자가 존재해야 합니다.");
        }
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("예약엔 이름이 존재해야 합니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약엔 날짜가 존재해야 합니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("예약엔 시간이 존재해야 합니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("예약엔 테마가 존재해야 합니다.");
        }
    }

    public UUID getTimeId() {
        return time.getId();
    }

    public UUID getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
