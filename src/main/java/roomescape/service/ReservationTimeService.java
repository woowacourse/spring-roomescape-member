package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.CreateReservationTimeRequest;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime addReservationTime(CreateReservationTimeRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        if (reservationTimeRepository.existsByTime(reservationTime.getTime())) {
            throw new InvalidReservationTimeException("중복된 예약시간입니다");
        }
        return reservationTimeRepository.add(reservationTime);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime getReservationTimeById(long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 예약 시간입니다."));
    }

    public void deleteReservationTime(Long id) {
        if (reservationTimeRepository.existsReservationByTimeId(id)) {
            throw new InvalidReservationTimeException("예약이 되어있는 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
