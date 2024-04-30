package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.exception.NotExistReservationTimeException;
import roomescape.service.dto.ReservationTimeInput;
import roomescape.service.dto.ReservationTimeOutput;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeOutput createReservationTime(ReservationTimeInput input) {
        ReservationTime reservationTime = input.toReservationTime();
        ReservationTime savedReservationTime = reservationTimeDao.create(reservationTime);
        return ReservationTimeOutput.toOutput(savedReservationTime);
    }

    public List<ReservationTimeOutput> getAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeDao.getAll();
        return ReservationTimeOutput.toOutputs(reservationTimes);
    }

    public void deleteReservationTime(long id) {
        ReservationTime reservationTime = reservationTimeDao.find(id)
                .orElseThrow(() -> new NotExistReservationTimeException(String.format("%d는 없는 id 입니다.", id)));

        reservationTimeDao.delete(reservationTime.getId());
    }
}
