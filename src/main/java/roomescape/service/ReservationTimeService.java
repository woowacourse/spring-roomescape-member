package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.exception.ExistsException;
import roomescape.exception.NotExistsException;
import roomescape.service.dto.input.ReservationTimeInput;
import roomescape.service.dto.output.AvailableReservationTimeOutput;
import roomescape.service.dto.output.ReservationTimeOutput;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao, final ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeOutput createReservationTime(final ReservationTimeInput input) {
        final ReservationTime reservationTime = input.toReservationTime();

        if (reservationTimeDao.isExistByStartAt(reservationTime.getStartAtAsString())) {
            throw ExistsException.of(String.format("startAt 이 %s 인 reservationTime", reservationTime.getStartAtAsString()));
        }

        final ReservationTime savedReservationTime = reservationTimeDao.create(reservationTime);
        return ReservationTimeOutput.from(savedReservationTime);
    }

    public List<ReservationTimeOutput> getAllReservationTimes() {
        final List<ReservationTime> reservationTimes = reservationTimeDao.getAll();
        return ReservationTimeOutput.list(reservationTimes);
    }

    public List<AvailableReservationTimeOutput> findAvailableReservationTimes(final String date, final long themeId) {
        return reservationTimeDao.findAvailable(ReservationDate.from(date), themeId);
    }

    public void deleteReservationTime(final long id) {
        final ReservationTime reservationTime = reservationTimeDao.findById(id)
                .orElseThrow(() -> NotExistsException.of("reservationTimeId", id));

        if (reservationDao.isExistByTimeId(id)) {
            throw ExistsException.of(String.format("reservationTimeId 가 %d 인 reservation", id));
        }

        reservationTimeDao.delete(reservationTime.id());
    }
}
