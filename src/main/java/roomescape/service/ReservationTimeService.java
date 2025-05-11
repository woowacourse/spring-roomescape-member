package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.entity.ReservationTime;
import roomescape.exception.impl.ConnectedReservationExistException;
import roomescape.exception.impl.HasDuplicatedTimeException;
import roomescape.exception.impl.ReservationTimeIntervalException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

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

    public ReservationTime createReservationTime(final LocalTime time) {
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

    public List<ReservationTime> getAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> getAvailableReservationTimesOf(final LocalDate date, final long themeId) {
        return reservationTimeRepository.getAvailableReservationTimeOf(date, themeId);
    }

    public boolean delete(final long id) {
        boolean isReservationExistInTime = reservationRepository.isExistByTimeId(id);
        if (isReservationExistInTime) {
            throw new ConnectedReservationExistException();
        }
        return reservationTimeRepository.deleteById(id);
    }
}
