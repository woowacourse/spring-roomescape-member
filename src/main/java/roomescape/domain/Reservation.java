package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Long themeId;

    private static final int MAX_NAME_LENGTH = 20;

    public Reservation(String name, LocalDate date, ReservationTime time, Long themeId) {
        this(null, name, date, time, themeId);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Long themeId) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
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

    public Long getThemeId() {
        return themeId;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("[ERROR] 이름은 공백일 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 이름은 %d자를 초과할 수 없습니다".formatted(MAX_NAME_LENGTH));
        }
    }
}
