package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.AddReservationTimeDto;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> allReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public Long addReservationTime(AddReservationTimeDto addReservationTimeDto) {
        ReservationTime reservationTime = addReservationTimeDto.toEntity();
        return reservationTimeRepository.add(reservationTime);
    }

    public ReservationTime findReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 id입니다."));
    }
}
