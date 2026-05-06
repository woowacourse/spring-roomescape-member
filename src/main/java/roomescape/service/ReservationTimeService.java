package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime create(LocalTime startAt) {
        Long id = reservationTimeRepository.insert(new ReservationTime(null, startAt));
        return reservationTimeRepository.findBy(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));
    }

    public void delete(Long id) {
        validateId(id);
        reservationTimeRepository.delete(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("[ERROR] id는 양수이어야 합니다.");
        }
    }
}
