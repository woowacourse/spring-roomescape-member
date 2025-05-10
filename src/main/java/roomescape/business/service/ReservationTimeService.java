package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.exception.business.InvalidCreateArgumentException;
import roomescape.exception.business.NotFoundException;
import roomescape.exception.business.RelatedEntityExistException;

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
            throw new InvalidCreateArgumentException("이미 존재하는 예약 시간입니다.");
        }
    }

    private void validateTimeInterval(final ReservationTime reservationTime) {
        boolean existInInterval = reservationTimeRepository.existBetween(reservationTime.startInterval(), reservationTime.endInterval());
        if (existInInterval) {
            throw new InvalidCreateArgumentException("예약 시간은 30분 간격으로만 생성할 수 있습니다.");
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
            throw new RelatedEntityExistException("해당 예약 시간의 예약이 존재합니다.");
        }
        if (!reservationTimeRepository.existById(id)) {
            throw new NotFoundException("존재하지 않는 예약 시간 입니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
