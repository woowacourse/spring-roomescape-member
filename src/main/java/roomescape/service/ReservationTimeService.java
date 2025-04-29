package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository jdbcReservationTimeRepository) {
        this.reservationTimeRepository = jdbcReservationTimeRepository;
    }

    public ReservationTime addTime(LocalTime startAt) {
        return reservationTimeRepository.addTime(startAt);
    }

    public List<ReservationTime> getAllTime() {
        return reservationTimeRepository.getAllTime();
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.deleteTime(id);
    }

    public ReservationTime getReservationTimeById(Long id) {
        return reservationTimeRepository.getReservationTimeById(id);
    }
}
