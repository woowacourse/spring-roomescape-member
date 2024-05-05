package roomescape.exception.time;

import org.springframework.http.HttpStatus;

import roomescape.exception.CustomException;

public class TimeDeletionException extends CustomException {
    public TimeDeletionException() {
        super("예약시간 삭제 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);
    }
}
