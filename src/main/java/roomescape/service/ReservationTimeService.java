package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest createDto) {
        ReservationTime time = createDto.toDomain();
        ReservationTime createdReservationTime = reservationTimeRepository.create(time);
        return ReservationTimeResponse.from(createdReservationTime);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeRepository.removeById(id);
    }
}
