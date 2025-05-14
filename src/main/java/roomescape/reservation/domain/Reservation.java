package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.common.domain.Id;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {
    private final Id id;
    private final LocalDate date;
    private final Member member;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Id id, final LocalDate date, final Member member, final ReservationTime time,
                        final Theme theme) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation of(final Long id, final LocalDate date, final Member member, final ReservationTime time,
                                 final Theme theme) {
        return new Reservation(Id.from(id), date, member, time, theme);
    }

    public static Reservation withUnassignedId(final LocalDate date, final Member member, final ReservationTime time,
                                               final Theme theme) {
        return new Reservation(Id.unassigned(), date, member, time, theme);
    }

    public Long getId() {
        return id.getValue();
    }

    public void setId(Long value) {
        id.setValue(value);
    }

    public Member getMember() {
        return member;
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
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
