package roomescape.exception;

public class ExistReservationInThemeException extends ExistReservationException {

    public ExistReservationInThemeException(final long id) {
        super(String.format("테마 ID %d에 해당하는 예약이 존재합니다.", id));
    }
}
