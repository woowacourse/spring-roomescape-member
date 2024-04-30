package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(final Long id, final Name name, final LocalDate date, final ReservationTime time) {
        validateDate(date);
        validateTime(date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    private void validateDate(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 잘못된 예약 날짜 입력입니다.");
        }

        LocalDate now = LocalDate.now();
        if (now.isAfter(date)) {
            throw new IllegalArgumentException("[ERROR] 현재 날짜보다 이전의 예약 날짜를 선택할 수 없습니다.");
        }
    }

    private void validateTime(final LocalDate date, final ReservationTime time) {
        LocalDate now = LocalDate.now();
        boolean isSameDate = now.isEqual(date);
        if (isSameDate && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 현재 시간보다 이전의 시간을 선택할 수 없습니다.");
        }
    }

    public static Reservation of(long id, String name, String date, long timeId) {
        LocalDate parsedDate = LocalDate.parse(date);

        return new Reservation(id, new Name(name), parsedDate, new ReservationTime(timeId));
    }

    public static Reservation of(long id, String name, String date, long timeId, String time) {
        LocalDate parsedDate = LocalDate.parse(date);
        LocalTime parsedTime = LocalTime.parse(time);

        return new Reservation(id, new Name(name), parsedDate, new ReservationTime(timeId, parsedTime));
    }

    public static Reservation of(final String name, final LocalDate date, final ReservationTime reservationTime) {
        return new Reservation(null, new Name(name), date, reservationTime);
    }

    public long getId() {
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

    public long getTimeId() {
        return time.getId();
    }
}
