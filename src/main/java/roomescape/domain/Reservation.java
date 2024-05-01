package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final TimeSlot time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, TimeSlot time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;

        isValid();
    }

    public Reservation(long id, String name, String date, String time) {
        this.id = id;
        this.name = name;
        this.date = LocalDate.parse(date);
        this.time = new TimeSlot(time);

        isValid();
    }

    private void isValid() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 이름은 비워둘 수 없습니다.");
        }

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 예약 날짜입니다.");
        }

        if (time == null || (time.isTimeBeforeNow() && !date.isAfter(LocalDate.now()))) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 예약 시간입니다.");
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

    public TimeSlot getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
