package roomescape.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Member member, final LocalDate date, final ReservationTime time, final Theme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation(final Long id, final Member member, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateDate(date);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createIfFuture(final LocalDateTime thresholdDateTime, final Member member, final LocalDate date, final ReservationTime time, final Theme theme) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(thresholdDateTime)) {
            throw new IllegalArgumentException(String.format("지나간 시간에 대한 예약은 생성할 수 없습니다. (%s)", reservationDateTime));
        }
        return new Reservation(member, date, time, theme);
    }

    private static void validateDate(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜가 비어 있습니다.");
        }
    }

    public boolean isBefore(final LocalDateTime dateTime) {
        final LocalDateTime reservationDateTime = getDateTime();
        return reservationDateTime.isBefore(dateTime);
    }

    private LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return member.getId();
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

    public Member getMember() {
        return member;
    }
}
