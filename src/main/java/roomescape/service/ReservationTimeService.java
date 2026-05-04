package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> allTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime saveTime(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.transientOf(startAt);
        return reservationTimeRepository.save(reservationTime);
    }

    public void removeTime(long timeId) {
        reservationTimeRepository.deleteById(timeId);
    }

    public ReservationTime findTime(long timeId) {
        return reservationTimeRepository.findById(timeId);
    }
}
