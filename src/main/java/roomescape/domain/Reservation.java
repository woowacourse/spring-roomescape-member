package roomescape.domain;

public class Reservation {

    private final Long id;
    private final Name name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Name name, ReservationDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, Name name, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public void validateDateTime() {
        if (date.isPastDate()) {
            throw new IllegalStateException("예약 날짜는 오늘보다 이전일 수 없습니다.");
        }

        if (date.isPresentDate() && time.isPastOrPresentTime()) {
            throw new IllegalStateException("예약 시간은 현재 시간보다 이전이거나 같을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
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
