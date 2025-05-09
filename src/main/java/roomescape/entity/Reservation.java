package roomescape.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private static final int NAME_LENGTH = 10;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    protected Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation createIfDateTimeValid(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateTheme(theme);
        validateDateTimeIsAfterNow(date, time);
        return new Reservation(null, name, date, time, theme);

    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약자명입니다.");
        }
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 예약자명의 길이가 " + NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 날짜입니다.");
        }
    }

    private static void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 시간입니다.");
        }
    }

    private static void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마입니다.");
        }
    }

    private static void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateTheme(theme);
        validateDate(date);
        validateTime(time);
    }

    private static void validateDateTimeIsAfterNow(LocalDate date, ReservationTime time) {
        validateDate(date);
        validateTime(time);
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 예약이 불가능한 시간입니다: " + date);
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

    public Long getTimeId() {
        if (time == null) {
            return null;
        }
        return time.getId();
    }

    public Long getThemeId() {
        if (theme == null) {
            return null;
        }
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
