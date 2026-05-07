package roomescape.domain;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private Time time;
    private Theme theme;

    public Reservation(String name, LocalDate date, Time time, Theme theme) {
        validate(name, date, time, theme);
        this.id = null;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation of(Long id, Reservation reservation) {
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    private void validate(String name, LocalDate date, Time time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수이며 비어있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("유효하지 않은 예약 시간대입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("유효하지 않은 테마입니다.");
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

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
