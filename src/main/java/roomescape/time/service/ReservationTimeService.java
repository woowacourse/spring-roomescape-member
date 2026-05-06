package roomescape.time.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationTime save(ReservationTimeCommand command) {
        if (reservationTimeRepository.existsByStartAt(command.startAt())) {
            throw new IllegalArgumentException("해당 시간이 이미 존재합니다.");
        }

        ReservationTime reservationTime = ReservationTime.create(command.startAt());
        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            reservationTimeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("예약에 사용 중인 시간은 삭제할 수 없습니다.");
        }
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }
}
