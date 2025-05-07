package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.exception.business.ConnectedReservationExistException;
import roomescape.exception.business.HasDuplicatedTimeException;
import roomescape.exception.business.ReservationTimeIntervalException;
import roomescape.exception.business.ReservationTimeNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            final ReservationTimeRepository reservationTimeRepository,
            final ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime addAndGet(final LocalTime time) {
        ReservationTime reservationTime = ReservationTime.create(time);
        validateNoDuplication(reservationTime);
        validateTimeInterval(reservationTime);

        reservationTimeRepository.save(reservationTime);
        return reservationTime;
    }

    private void validateNoDuplication(final ReservationTime reservationTime) {
        boolean isExist = reservationTimeRepository.existByTime(reservationTime.startAt());
        if (isExist) {
            throw new HasDuplicatedTimeException();
        }
    }

    private void validateTimeInterval(final ReservationTime reservationTime) {
        boolean existInInterval = reservationTimeRepository.existBetween(reservationTime.startInterval(), reservationTime.endInterval());
        if (existInInterval) {
            throw new ReservationTimeIntervalException();
        }
    }

    public List<ReservationTime> getAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> getAvailableReservationTimesByDateAndThemeId(final LocalDate date, final String themeId) {
        return reservationTimeRepository.findAvailableReservationTimesByDateAndThemeId(date, themeId);
    }

    public void delete(final String id) {
        if (reservationRepository.existByTimeId(id)) {
            throw new ConnectedReservationExistException();
        }
        if (!reservationTimeRepository.existById(id)) {
            throw new ReservationTimeNotFoundException();
        }
        reservationTimeRepository.deleteById(id);
    }
}
