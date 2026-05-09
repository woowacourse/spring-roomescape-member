package roomescape.domain;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class Reservation {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.theme = theme;
        validateNameFormat(name);
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    private void validateNameFormat(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 입력되지 않았습니다. 이름을 다시 입력해주세요.");
        }
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름 형식은 2글자 이상 10글자 이하입니다");
        }
    }
}
