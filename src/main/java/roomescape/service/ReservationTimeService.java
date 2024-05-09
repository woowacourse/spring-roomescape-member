package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.infrastructure.persistence.ReservationRepository;
import roomescape.infrastructure.persistence.ReservationTimeRepository;
import roomescape.service.request.ReservationTimeRequest;
import roomescape.service.response.AvailableReservationTimeResponse;
import roomescape.service.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toDomain();
        if (reservationTimeRepository.hasDuplicateTime(reservationTime)) {
            throw new IllegalStateException("해당 예약 시간이 존재합니다.");
        }

        ReservationTime createdReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(createdReservationTime);
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.hasByTimeId(id)) {
            throw new IllegalStateException("해당 시간을 사용하고 있는 예약이 존재합니다.");
        }

        reservationTimeRepository.removeById(id);
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> getAvailableReservationTimes(String date, Long themeId) {
        return reservationTimeRepository.findAvailableReservationTimes(LocalDate.parse(date), themeId);
    }
}
