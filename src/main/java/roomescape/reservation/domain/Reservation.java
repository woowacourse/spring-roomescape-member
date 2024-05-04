package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.regex.Pattern;
import roomescape.exception.BadRequestException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

public class Reservation {
    private static final Pattern ILLEGAL_NAME_REGEX = Pattern.compile(".*[^\\w\\s가-힣].*");

    private long id;
    private final String name;
    private final LocalDate date;
    private Time time;
    private Theme theme;

    public Reservation(String name, LocalDate date, long timeId, long themeId) {
        this(0, name, date, new Time(timeId), new Theme(themeId));
        validate();
    }

    public Reservation(long id, String name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public long getId() {
        return id;
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

    public void setIdOnSave(long id) {
        this.id = id;
    }

    public void setTimeOnSave(Time time) {
        if (date.equals(LocalDate.now())) {
            validateTime(time);
        }
        this.time = time;
    }

    public void setThemeOnSave(Theme theme) {
        this.theme = theme;
    }

    public boolean hasSameId(long id) {
        return this.id == id;
    }

    private void validateTime(Time time) {
        if (time.getStartAt().isBefore(LocalTime.now())) {
            throw new BadRequestException("지난 시간의 테마를 선택했습니다.");
        }
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("null 혹은 빈칸으로 이루어진 이름으로 예약을 시도하였습니다.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new BadRequestException("지난 날짜의 예약을 시도하였습니다.");
        }
        if (ILLEGAL_NAME_REGEX.matcher(name)
                .matches()) {
            throw new BadRequestException("특수문자가 포함된 이름으로 예약을 시도하였습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
