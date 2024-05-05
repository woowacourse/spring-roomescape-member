package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;
import roomescape.exception.BadRequestException;
import roomescape.exception.IllegalReservationDateTimeRequestException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

public class Reservation {

    private static final Pattern ILLEGAL_NAME_REGEX = Pattern.compile(".*[^\\w\\s가-힣].*");
    private final String name;
    private final LocalDate date;
    private Theme theme;
    private long id;
    private Time time;

    public Reservation(String name, LocalDate date, Time time, Theme theme) {
        this(0, name, date, time, theme);
        validate(name, date, time, theme);
    }

    public Reservation(long id, String name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public void validate(String name, LocalDate date, Time time, Theme theme) {
        if (name == null || date == null || time == null || theme == null) {
            throw new BadRequestException("예약 정보가 부족합니다.");
        }
        validateName(name);
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalReservationDateTimeRequestException("지난 날짜의 예약을 시도하였습니다.");
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new BadRequestException("공백으로 이루어진 이름으로 예약할 수 없습니다.");
        }
        if (ILLEGAL_NAME_REGEX.matcher(name)
                .matches()) {
            throw new BadRequestException("특수문자가 포함된 이름으로 예약을 시도하였습니다.");
        }
    }

    public boolean hasSameId(long id) {
        return this.id == id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getReservationTime() {
        return time;
    }

    public long getReservationTimeId() {
        return time.getId();
    }

    public Theme getTheme() {
        return theme;
    }

    public long getThemeId() {
        return theme.getId();
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(
                theme, that.theme) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, theme, id, time);
    }

}
