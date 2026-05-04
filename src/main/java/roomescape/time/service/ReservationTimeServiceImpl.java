package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeServiceImpl implements ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeServiceImpl(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime createTime(LocalTime startAt) {
        return reservationTimeRepository.save(startAt);
    }

    public List<ReservationTime> getTimes() {
        return reservationTimeRepository.findAll();
    }

    public void removeTime(Long id) {
        reservationTimeRepository.remove(id);
    }
}
