package roomescape.domain;

import java.time.LocalDateTime;

public class Reservation {
    private final long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(long id, Name name, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(long id, Name name, ReservationDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation of(Name name, ReservationDate date, ReservationTime time, Theme theme) {
        return new Reservation(0L, name, date, time, theme);
    }

    public static Reservation reserve(Name name, ReservationDate date, ReservationTime time, Theme theme,
                                      LocalDateTime now) {
        validateIsNotNull(now);
        validateAvailableDateTime(date, time, now);
        return new Reservation(0L, name, date, time, theme);
    }

    private static void validateIsNotNull(LocalDateTime now) {
        if (now == null) {
            throw new IllegalStateException("현재 날짜를 입력해야 합니다.");
        }
    }


    private static void validateAvailableDateTime(ReservationDate requestDate, ReservationTime requestTime,
                                                  LocalDateTime now) {
        if (requestDate.isBefore(now.toLocalDate())) {
            throw new IllegalArgumentException("지나간 날짜에 대한 예약 생성은 불가능합니다.");
            // TODO: 예외 처리 클래스 및 메시지
        }

        if (requestDate.isEqual(now.toLocalDate()) && requestTime.isBefore(now.toLocalTime())) {
            throw new IllegalArgumentException("지나간 시에 대한 예약 생성은 불가능합니다.");
            // TODO: 예외 처리 클래스 및 메시지
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
