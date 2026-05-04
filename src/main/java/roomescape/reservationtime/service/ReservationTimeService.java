package roomescape.reservationtime.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime save(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.createNew(startAt);

        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new IllegalArgumentException("[ERROR] 같은 시간을 추가할 수 없습니다.");
        }

        return reservationTimeRepository.save(reservationTime);
    }

    public void deleteById(long id) {
        reservationTimeRepository.deleteById(id);
    }

    public ReservationTime getById(long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 찾는 시간이 없습니다"));
    }

    public List<ReservationTime> getAll() {
        return reservationTimeRepository.findAll();
    }

}
