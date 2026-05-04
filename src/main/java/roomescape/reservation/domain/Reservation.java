package roomescape.reservation.domain;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비어 있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 비어 있을 수 없습니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 비어 있을 수 없습니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
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
}
