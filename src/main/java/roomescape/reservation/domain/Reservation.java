package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;

import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public record Reservation(long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
    private static final long UNDEFINED = 0;

    public Reservation {
        validateName(member.name());
    }

    public Reservation(Member member, LocalDate date, ReservationTime time, Theme theme) {
        this(UNDEFINED, member, date, time, theme);
    }

    private String validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("Name cannot exceed 10 characters");
        }
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Reservation target = (Reservation) object;
        return id == target.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
