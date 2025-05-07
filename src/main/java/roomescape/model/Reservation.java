package roomescape.model;

public class Reservation {
    private final Long id;
    private final MemberName memberName;
    private final ReservationDateTime reservationDateTime;
    private final Theme theme;

    public Reservation(Long id, MemberName memberName, ReservationDateTime reservationDateTime, Theme theme) {
        validateNotNull(memberName, reservationDateTime, theme);
        this.id = id;
        this.memberName = memberName;
        this.reservationDateTime = reservationDateTime;
        this.theme = theme;
    }

    private void validateNotNull(final MemberName memberName, final ReservationDateTime reservationDateTime,
                                 final Theme theme) {
        if (memberName == null || reservationDateTime == null || theme == null) {
            throw new IllegalArgumentException("예약 생성 시 사용자, 시간, 테마는 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public MemberName getUserName() {
        return memberName;
    }

    public ReservationDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
