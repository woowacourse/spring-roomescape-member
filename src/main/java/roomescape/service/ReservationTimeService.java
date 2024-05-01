package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.request.ReservationTimeRequest;
import roomescape.service.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest createDto) {
        ReservationTime time = createDto.toDomain();
        if (reservationTimeRepository.hasDuplicateTime(time)) {
            throw new IllegalStateException("해당 예약 시간이 존재합니다.");
        }
        ReservationTime createdReservationTime = reservationTimeRepository.create(time);
        return ReservationTimeResponse.from(createdReservationTime);
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.hasByTimeId(id)) {
            throw new IllegalStateException("해당 시간을 사용하고 있는 예약이 존재합니다.");
        }

        reservationTimeRepository.removeById(id);
    }
}
