package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class Reservation {

    private final Long id;
    private final UserName name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, UserName name, LocalDate date, ReservationTime time) {
        validate(date, time.getStartAt());
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(UserName name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
    }

    private void validate(LocalDate date, LocalTime time) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜가 입력되지 않았습니다.");
        }
        LocalDate nowDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalTime nowTime = LocalTime.now(ZoneId.of("Asia/Seoul"));
        if (date.isBefore(nowDate) || (date.isEqual(nowDate) && time.isBefore(nowTime))) {
            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }
}
