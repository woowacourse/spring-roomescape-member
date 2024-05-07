package roomescape.exception;

import org.springframework.dao.DataAccessException;

public class ReservationExistsException extends DataAccessException {

    public ReservationExistsException() {
        super("해당 데이터를 사용하는 예약이 이미 존재하여 삭제할 수 없습니다.");
    }
}
