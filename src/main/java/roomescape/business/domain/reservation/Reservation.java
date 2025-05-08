package roomescape.business.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.business.domain.member.MemberName;
import roomescape.business.domain.theme.Theme;

public final class Reservation {

    private final Long id;
    private final MemberName name;
    private final ReservationDateTime dateTime;
    private final Theme theme;

    private Reservation(final Long id, final MemberName name, final ReservationDateTime dateTime, final Theme theme) {
        validateNotNull(name, dateTime, theme);
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.theme = theme;
    }

    public Reservation(final Long id, final String name,
                       final LocalDate date, final ReservationTime time,
                       final Theme theme) {
        this(id, new MemberName(name), new ReservationDateTime(date, time), theme);
    }

    public Reservation(final String name, final LocalDate date,
                       final ReservationTime time, final Theme theme) {
        this(null, new MemberName(name), new ReservationDateTime(date, time), theme);
        validateDateTime();
    }

    public Reservation(final long id, final Reservation reservation) {
        this(id, reservation.name, reservation.dateTime, reservation.theme);
    }

    private void validateNotNull(final MemberName name, final ReservationDateTime dateTime, final Theme theme) {
        if (name == null) {
            throw new IllegalArgumentException("이름을 입력해야 합니다.");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("날짜와 시간을 입력해야 합니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("테마를 입력해야 합니다.");
        }
    }

    private void validateDateTime() {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간은 예약 불가합니다.");
        }
    }

    public Long getTimeId() {
        return dateTime.getTimeId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return dateTime.getDate();
    }

    public ReservationTime getTime() {
        return dateTime.getTime();
    }

    public Theme getTheme() {
        return theme;
    }

    public long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Reservation that = (Reservation) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
