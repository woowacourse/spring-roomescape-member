package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public ReservationTime create(LocalTime startAt) {
        Long id = reservationTimeRepository.insert(new ReservationTime(startAt));
        return reservationTimeRepository.findBy(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 예약 시간입니다."));
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 ID입니다.");
        }
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 예약이 존재합니다.");
        }
        reservationTimeRepository.delete(id);
    }
}
