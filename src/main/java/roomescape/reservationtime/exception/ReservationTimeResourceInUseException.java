package roomescape.reservationtime.exception;


import roomescape.common.exception.ResourceInUseException;

public class ReservationTimeResourceInUseException extends ResourceInUseException {

    public ReservationTimeResourceInUseException(Long id) {
        super("예약이 존재하는 시간은 삭제할 수 없습니다. id=" + id);
    }
}
