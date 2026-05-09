package roomescape.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime saveTime(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.create(startAt);
        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTime> getTimes() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> getBookedTimes(LocalDate date, Long themeId) {
        if (date == null || themeId == null) {
            return List.of();
        }
        return reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::time)
                .toList();
    }

    @Transactional
    public void deleteTime(Long id) {
        if (reservationRepository.existsByReservationTimeId(id)) {
            throw new ReservationTimeException(ErrorCode.RESERVATION_TIME_ALREADY_USED);
        }
        reservationTimeRepository.deleteById(id);
    }
}
