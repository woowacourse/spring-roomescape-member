package roomescape.business.model.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import roomescape.business.model.vo.Id;
import roomescape.exception.business.InvalidCreateArgumentException;

import java.time.LocalDate;
import java.time.Period;

@ToString
@EqualsAndHashCode(exclude = "id")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Reservation {

    private static final int DAY_INTERVAL_FROM_NOW = 7;

    private final Id id;
    private final User user;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public static Reservation create(final User user, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateDateIsNotPast(date);
        validateDateInterval(date);
        return new Reservation(Id.issue(), user, date, time, theme);
    }

    private static void validateDateIsNotPast(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidCreateArgumentException("과거 날짜로 예약할 수 없습니다.");
        }
    }

    private static void validateDateInterval(final LocalDate date) {
        long minusDays = Period.between(LocalDate.now(), date).getDays();
        if (minusDays > DAY_INTERVAL_FROM_NOW) {
            throw new InvalidCreateArgumentException("일주일 전부터 예약할 수 있습니다.");
        }
    }

    public static Reservation restore(final String id, final User user, final LocalDate date, final ReservationTime time, final Theme theme) {
        return new Reservation(Id.create(id), user, date, time, theme);
    }

    public boolean isSameReserver(final String userId) {
        return user.id().equals(userId);
    }

    public String id() {
        return id.value();
    }

    public String reserverName() {
        return user.name();
    }

    public LocalDate date() {
        return date;
    }

    public ReservationTime time() {
        return time;
    }

    public Theme theme() {
        return theme;
    }

    public User reserver() {
        return user;
    }
}
