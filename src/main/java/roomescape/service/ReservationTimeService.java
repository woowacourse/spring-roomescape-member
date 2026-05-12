package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
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
        validateDuplicateStartAt(startAt);
        return reservationTimeRepository.insert(new ReservationTime(startAt));
    }

    private void validateDuplicateStartAt(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new ConflictException("이미 추가된 예약 시간대입니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new NotFoundException("존재하지 않는 예약 시간대입니다.");
        }
        if (reservationRepository.existsByTimeId(id)) {
            throw new ConflictException("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeRepository.delete(id);
    }
}
