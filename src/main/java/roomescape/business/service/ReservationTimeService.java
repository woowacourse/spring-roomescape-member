package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.exception.impl.ConnectedReservationExistException;
import roomescape.exception.impl.HasDuplicatedTimeException;
import roomescape.exception.impl.ReservationTimeIntervalException;
import roomescape.exception.impl.ReservationTimeNotFoundException;

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
        validateNoDuplication(time);
        validateTimeInterval(time);

        ReservationTime reservationTime = ReservationTime.beforeSave(time);
        return reservationTimeRepository.save(reservationTime);
    }

    private void validateNoDuplication(final LocalTime createTime) {
        boolean isExist = reservationTimeRepository.existByTime(createTime);
        if (isExist) {
            throw new HasDuplicatedTimeException();
        }
    }

    private void validateTimeInterval(final LocalTime createTime) {
        boolean hasLess30MinDifference = reservationTimeRepository.findAll().stream()
                .anyMatch(reservationTime -> reservationTime.isInTimeInterval(createTime));
        if (hasLess30MinDifference) {
            throw new ReservationTimeIntervalException();
        }
    }

    public List<ReservationTime> getAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> getAvailableReservationTimesByDateAndThemeId(final LocalDate date, final long themeId) {
        return reservationTimeRepository.findAvailableReservationTimesByDateAndThemeId(date, themeId);
    }

    public void delete(final long id) {
        if (reservationRepository.existByTimeId(id)) {
            throw new ConnectedReservationExistException();
        }
        if (!reservationTimeRepository.existById(id)) {
            throw new ReservationTimeNotFoundException();
        }
        reservationTimeRepository.deleteById(id);
    }
}
