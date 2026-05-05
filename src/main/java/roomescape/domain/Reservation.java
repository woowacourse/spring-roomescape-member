package roomescape.domain;

import roomescape.domain.vo.Name;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reservation {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation() {
        this.id = null;
        this.name = null;
        this.date = null;
        this.time = null;
        this.theme = null;
    }

    public Reservation(Long id, Name name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Long id, String name, String date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = new Name(name);
        this.date = translateDate(date);
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, String date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, new Name(name), date, time, theme);
    }

    private LocalDate translateDate(String date) {
        Matcher matcher = DATE_PATTERN.matcher(date);
        if (!matcher.matches()){
            throw new IllegalArgumentException("날짜는 yyyy-MM-dd 형태여야 하는데, 현재 다음과 같이 잘못 입력되었습니다: " + date);
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("실제 존재하지 않는 날짜입니다: " + date);
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
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

    public Long getThemeId() {
        return theme.getId();
    }

    public Reservation withId(Long key) {
        return new Reservation(key, this.name, this.date, this.time, this.theme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}