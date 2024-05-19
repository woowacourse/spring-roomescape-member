package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.exception.CustomBadRequest;
import roomescape.service.dto.input.ReservationTimeInput;
import roomescape.service.dto.output.AvailableReservationTimeOutput;
import roomescape.service.dto.output.ReservationTimeOutput;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao,
                                  final ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeOutput createReservationTime(final ReservationTimeInput input) {
        final var reservationTime = input.toReservationTime();
        checkReservationTimeNotExists(reservationTime);
        final var savedReservationTime = reservationTimeDao.create(reservationTime);
        return ReservationTimeOutput.from(savedReservationTime);
    }

    private void checkReservationTimeNotExists(final ReservationTime time) {
        if (reservationTimeDao.existsByStartAt(time.getStartAtAsString())) {
            throw new CustomBadRequest(String.format("예약 시간(startAt=%s)이 존재합니다.", time.getStartAtAsString()));
        }
    }

    public List<ReservationTimeOutput> getAllReservationTimes() {
        final var reservationTimes = reservationTimeDao.getAll();
        return ReservationTimeOutput.list(reservationTimes);
    }

    public ReservationTime getReservationTimeById(final Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new CustomBadRequest(String.format("timeId(%s)가 존재하지 않습니다.", timeId)));
    }

    public List<AvailableReservationTimeOutput> findAvailableReservationTimes(final String date, final long themeId) {
        final var availableTimes = reservationTimeDao.findAvailable(ReservationDate.from(date), themeId);
        return AvailableReservationTimeOutput.list(availableTimes);
    }

    public void deleteReservationTime(final long id) {
        final var time = getReservationTimeById(id);
        checkReservationNotExists(time);
        reservationTimeDao.delete(id);
    }

    public void checkReservationNotExists(final ReservationTime time) {
        if (reservationDao.exists(time)) {
            throw new CustomBadRequest(String.format("예약(timeId=%s)이 존재합니다.", time.id()));
        }
    }
}
