package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationDate;
import roomescape.repository.ReservationRepository;
import roomescape.service.dto.input.ReservationTimeInput;
import roomescape.service.dto.output.AvailableReservationTimeOutput;
import roomescape.service.dto.output.ReservationTimeOutput;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationRepository reservationRepository,
                                  final ReservationTimeDao reservationTimeDao) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeOutput createReservationTime(final ReservationTimeInput input) {
        final var reservationTime = input.toReservationTime();
        reservationRepository.checkReservationTimeNotExists(reservationTime);
        final var savedReservationTime = reservationTimeDao.create(reservationTime);
        return ReservationTimeOutput.from(savedReservationTime);
    }

    public List<ReservationTimeOutput> getAllReservationTimes() {
        final var reservationTimes = reservationTimeDao.getAll();
        return ReservationTimeOutput.list(reservationTimes);
    }

    public List<AvailableReservationTimeOutput> findAvailableReservationTimes(final String date, final long themeId) {
        final var availableTimes = reservationTimeDao.findAvailable(ReservationDate.from(date), themeId);
        return AvailableReservationTimeOutput.list(availableTimes);
    }

    public void deleteReservationTime(final long id) {
        final var time = reservationRepository.getReservationTimeById(id);
        reservationRepository.checkReservationNotExists(time);
        reservationTimeDao.delete(id);
    }
}
