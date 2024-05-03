package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private ReservationTimeRepository reservationTimeRepository;

    ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        if (reservationTimeRepository.existByStartAt(reservationTimeAddRequest.getStartAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약시간은 추가할 수 없습니다.");
        }
        return reservationTimeRepository.insert(reservationTimeAddRequest.toEntity());
    }

    public void removeReservationTime(Long id) {
        if (reservationTimeRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약시간이 존재하지 않습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
