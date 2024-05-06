package roomescape.model;

import roomescape.service.dto.ReservationDto;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation(long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(id, name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.id = 0;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private Reservation() {
    }

    public static Reservation from(ReservationDto reservationDto, ReservationTime time, Theme theme) {
        return new Reservation(reservationDto.getName(), reservationDto.getDate(), time, theme);
    }

    private void validate(long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateRange(id);
        validate(name, date, time, theme);
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateNull(name);
        validateEmpty(name);
        validateNull(date);
        validateNull(time);
        validateNull(theme);
    }


    private void validateRange(long id) {
        if (id <= 0) {
            throw new IllegalStateException("[ERROR] id는 0 이하일 수 없습니다.");
        }
    }

    private void validateNull(Object value) {
        if (value == null) {
            throw new IllegalStateException("[ERROR] 데이터는 null 혹은 빈 문자열일 수 없습니다.");
        }
    }

    private void validateEmpty(String value) {
        if (value.isBlank()) {
            throw new IllegalStateException("[ERROR] 데이터는 null 혹은 빈 문자열일 수 없습니다.");
        }
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
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time, theme);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
