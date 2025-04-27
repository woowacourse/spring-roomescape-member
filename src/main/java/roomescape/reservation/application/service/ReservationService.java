package roomescape.reservation.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.domain.aggregate.ReservationDate;
import roomescape.reservation.domain.aggregate.ReservationName;
import roomescape.reservation.domain.aggregate.ReservationTime;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                new ReservationName(reservationRequest.getName()),
                new ReservationDate(reservationRequest.getDate()),
                findReservationTime(reservationRequest.getTimeId())
        );

        return new ReservationResponse(reservationRepository.insert(createReservationRequest));
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 예약 시간 정보를 찾을 수 없습니다."));
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAllReservations().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteReservation(final Long id) {
        reservationRepository.delete(id);
    }
}
