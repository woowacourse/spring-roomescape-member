package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final String name, final LocalDate date,
                        final ReservationTime time, final Theme theme
    ) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createWithoutId(final LocalDateTime now, final String name,
                                              final LocalDate reservationDate,
                                              final ReservationTime time, final Theme theme
    ) {
        validateReservationDateTime(now, reservationDate, time);
        return new Reservation(null, name, reservationDate, time, theme);
    }

    private static void validateReservationDateTime(LocalDateTime now, LocalDate reservationDate,
                                                    ReservationTime time) {
        LocalDate nowDate = now.toLocalDate();
        if (reservationDate.isBefore(nowDate)) {
            throw new IllegalArgumentException("예약할 수 없는 날짜와 시간입니다.");
        }

        LocalTime nowTime = now.toLocalTime();
        if (nowDate.isEqual(reservationDate) && time.isBeforeTime(nowTime)) {
            throw new IllegalArgumentException("예약할 수 없는 날짜와 시간입니다.");
        }
    }

    public static Reservation createWithId(final Long id, final String name, final LocalDate date,
                                           final ReservationTime time, final Theme theme
    ) {
        return new Reservation(Objects.requireNonNull(id), name, date, time, theme);
    }

    public Reservation assignId(final Long id) {
        return new Reservation(Objects.requireNonNull(id), name, date, time, theme);
    }

    public boolean isSameTime(final ReservationTime time) {
        return this.time.isSameTime(time);
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

    public Long getTimeId() {
        return time.getId();
    }

    public LocalTime getReservationTime() {
        return time.getStartAt();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public String getThemeDescription() {
        return theme.getDescription();
    }

    public String getThemeName() {
        return theme.getName();
    }

    public String getThemeThumbnail() {
        return theme.getThumbnail();
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Reservation that)) {
            return false;
        }

        if (getId() == null && that.getId() == null) {
            return false;
        }

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
