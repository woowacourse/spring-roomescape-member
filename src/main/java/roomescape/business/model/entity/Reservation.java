package roomescape.business.model.entity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import roomescape.business.model.vo.Id;
import roomescape.business.model.vo.ReservationDate;

import java.time.LocalDate;
import java.util.Objects;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Reservation {

    private final Id id;
    private final User user;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public static Reservation create(final User user, final LocalDate date, final ReservationTime time, final Theme theme) {
        return new Reservation(Id.issue(), user, new ReservationDate(date), time, theme);
    }

    public static Reservation restore(final String id, final User user, final LocalDate date, final ReservationTime time, final Theme theme) {
        return new Reservation(Id.create(id), user, new ReservationDate(date), time, theme);
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
        return date.value();
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

    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof final Reservation that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
