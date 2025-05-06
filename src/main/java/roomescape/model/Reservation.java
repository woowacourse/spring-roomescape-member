package roomescape.model;

public class Reservation {
    private final Long id;
    private final UserName userName;
    private final ReservationDateTime reservationDateTime;
    private final Theme theme;

    public Reservation(Long id, UserName userName, ReservationDateTime reservationDateTime, Theme theme) {
        validateNotNull(userName, reservationDateTime, theme);
        this.id = id;
        this.userName = userName;
        this.reservationDateTime = reservationDateTime;
        this.theme = theme;
    }

    private void validateNotNull(final UserName userName, final ReservationDateTime reservationDateTime,
                                  final Theme theme) {
        if (userName == null || reservationDateTime == null || theme == null) {
            throw new IllegalArgumentException("예약 생성 시 사용자, 시간, 테마는 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public UserName getUserName() {
        return userName;
    }

    public ReservationDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
