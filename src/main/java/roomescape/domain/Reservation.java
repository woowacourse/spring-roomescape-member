package roomescape.domain;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reservation {
    private static final Pattern NAME_PATTERN = Pattern.compile("^\\d+$");

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, String date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, Reservation reservation) {
        this.id = id;
        this.name = reservation.name;
        this.date = reservation.date;
        this.time = reservation.time;
        this.theme = reservation.theme;
    }

    public Reservation(Long id, String name, String dateInput, ReservationTime time, Theme theme) {
        validateName(name);
        LocalDate date = convertToLocalDate(dateInput);
        validateDate(date);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비어있을 수 없습니다.");
        }
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (matcher.matches()) {
            throw new IllegalArgumentException("예약자 이름은 숫자로만 구성될 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now()) || date.equals(LocalDate.now())) {
            throw new IllegalArgumentException("이전 날짜 혹은 당일은 예약할 수 없습니다.");
        }
    }

    private LocalDate convertToLocalDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("예약 날짜가 비어있습니다.");
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 예약 날짜입니다.");
        }
    }

    public boolean hasSameDateTime(LocalDate date, ReservationTime time) {
        return this.time.equals(time) && this.date.equals(date);
    }

    public Long getReservationTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
