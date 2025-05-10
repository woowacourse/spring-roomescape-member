package roomescape.model;

public class Reservation {
    private final Long id;
    private final Member member;
    private final ReservationDateTime reservationDateTime;
    private final Theme theme;

    public Reservation(Long id, Member member, ReservationDateTime reservationDateTime, Theme theme) {
        validateNotNull(member, reservationDateTime, theme);
        this.id = id;
        this.member = member;
        this.reservationDateTime = reservationDateTime;
        this.theme = theme;
    }

    private void validateNotNull(final Member member, final ReservationDateTime reservationDateTime,
                                 final Theme theme) {
        if (member == null || reservationDateTime == null || theme == null) {
            throw new IllegalArgumentException("예약 생성 시 사용자, 시간, 테마는 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public ReservationDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
