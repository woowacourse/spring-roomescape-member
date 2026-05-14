package roomescape.service;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.ConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
@RequiredArgsConstructor
public class ReservationTimeCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeResponse create(LocalTime startAt) {
        ReservationTime savedReservationTime = reservationTimeDao.save(ReservationTime.pending(startAt));
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void delete(long reservationTimeId) {
        if (reservationDao.existsByTimeId(reservationTimeId)) {
            throw new ConflictException(ErrorMessage.TIME_IN_USE);
        }
        reservationTimeDao.deleteByTimeId(reservationTimeId);
    }
}
