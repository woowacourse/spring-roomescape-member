package roomescape.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.exception.business.DuplicatedException;
import roomescape.exception.business.InvalidCreateArgumentException;
import roomescape.exception.business.NotFoundException;
import roomescape.exception.business.RelatedEntityExistException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static roomescape.exception.ErrorCode.RESERVATION_NOT_EXIST;
import static roomescape.exception.ErrorCode.RESERVATION_TIME_ALREADY_EXIST;
import static roomescape.exception.ErrorCode.RESERVATION_TIME_INTERVAL_INVALID;
import static roomescape.exception.ErrorCode.RESERVED_RESERVATION_TIME;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

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
            throw new DuplicatedException(RESERVATION_TIME_ALREADY_EXIST);
        }
    }

    private void validateTimeInterval(final ReservationTime reservationTime) {
        boolean existInInterval = reservationTimeRepository.existBetween(reservationTime.startInterval(), reservationTime.endInterval());
        if (existInInterval) {
            throw new InvalidCreateArgumentException(RESERVATION_TIME_INTERVAL_INVALID);
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
            throw new RelatedEntityExistException(RESERVED_RESERVATION_TIME);
        }
        if (!reservationTimeRepository.existById(id)) {
            throw new NotFoundException(RESERVATION_NOT_EXIST);
        }
        reservationTimeRepository.deleteById(id);
    }
}
