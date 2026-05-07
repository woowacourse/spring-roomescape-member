package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public ReservationTime create(LocalTime startAt) {
        Long id = reservationTimeRepository.insert(new ReservationTime(null, startAt));
        return reservationTimeRepository.findBy(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 ID입니다.");
        }
        reservationTimeRepository.delete(id);
    }
}
