package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ReservedTimeChecker;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservedTimeChecker reservedTimeChecker;

    public ReservationTimeService(ReservationTimeRepository jdbcReservationTimeRepository,
                                  ReservedTimeChecker reservedTimeChecker) {
        this.reservationTimeRepository = jdbcReservationTimeRepository;
        this.reservedTimeChecker = reservedTimeChecker;
    }

    public ReservationTime addTime(LocalTime startAt) {
        return reservationTimeRepository.addTime(startAt);
    }

    public List<ReservationTime> getAllTime() {
        return reservationTimeRepository.getAllTime();
    }

    public void deleteTime(Long id) {
        if (reservedTimeChecker.isReserved(id)) {
            throw new IllegalArgumentException("Reservation time is already reserved.");
        }
        reservationTimeRepository.deleteTime(id);
    }

    public ReservationTime getReservationTimeById(Long id) {
        return reservationTimeRepository.getReservationTimeById(id);
    }
}
