package roomescape.exception.reservation;

import org.springframework.http.HttpStatus;

import roomescape.exception.CustomException;

public class ReservationDeletionException extends CustomException {
    public ReservationDeletionException() {
        super("예약 삭제 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);
    }
}
