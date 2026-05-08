package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final Clock clock;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, Clock clock) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.clock = clock;
    }

    public ReservationTime saveReservationTime(ReservationTime reservationTime) {
        return reservationTimeRepository.addTime(reservationTime);
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes();
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteTime(id);
    }

    public List<ReservationTime> getAvailableTimes(Long themeId, LocalDate date) {
        LocalDate selectedDate = Objects.isNull(date) ? LocalDate.now(clock) : date;
        return reservationTimeRepository.findAvailableTimes(themeId, selectedDate);
    }
}
