package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
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
        return reservationTimeRepository.findAvailableTimes(themeId, date);
    }
}
