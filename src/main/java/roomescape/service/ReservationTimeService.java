package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.dto.AvailableReservationTimeResponse;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.exception.ExistReservationInReservationTimeException;
import roomescape.exception.NotExistReservationTimeException;
import roomescape.exception.ReservationTimeAlreadyExistsException;
import roomescape.service.dto.input.AvailableReservationTimeInput;
import roomescape.service.dto.input.ReservationTimeInput;
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
            throw new ReservationTimeAlreadyExistsException(reservationTime.getStartAtAsString());
        }

        final ReservationTime savedReservationTime = reservationTimeDao.create(reservationTime);
        return ReservationTimeOutput.toOutput(savedReservationTime);
    }

    public List<ReservationTimeOutput> getAllReservationTimes() {
        final List<ReservationTime> reservationTimes = reservationTimeDao.getAll();
        return ReservationTimeOutput.toOutputs(reservationTimes);
    }

    public List<AvailableReservationTimeResponse> getAvailableTimes(final AvailableReservationTimeInput input) {
        return reservationTimeDao.getAvailable(ReservationDate.from(input.date()), input.themeId());
    }

    public void deleteReservationTime(final long id) {
        final ReservationTime reservationTime = reservationTimeDao.find(id)
                                                                  .orElseThrow(() -> new NotExistReservationTimeException(id));

        if (reservationDao.isExistByTimeId(id)) {
            throw new ExistReservationInReservationTimeException(id);
        }

        reservationTimeDao.delete(reservationTime.getId());
    }
}
