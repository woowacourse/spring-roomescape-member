package roomescape.service.reservationtime;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationTimeFindService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeFindService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public Map<ReservationTime, Boolean> findIsBooked(LocalDate date, Long themeId) {
        List<ReservationTime> reservedTimes = reservationTimeRepository.findReservedBy(date, themeId);
        Map<ReservationTime, Boolean> isBooked = new HashMap<>();
        for (ReservationTime reservationTime : reservationTimeRepository.findAll()) {
            isBooked.put(reservationTime, isReserved(reservedTimes, reservationTime));
        }
        return isBooked;
    }

    private boolean isReserved(List<ReservationTime> reservedTimes, ReservationTime reservationTime) {
        return reservedTimes.stream()
                .anyMatch(reservedTime -> reservedTime.isSameReservationTime(reservationTime.getId()));
    }
}
