package roomescape.reservation.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse createReservationTime(final ReservationTimeRequest reservationTimeRequest) {
        return new ReservationTimeResponse(reservationTimeRepository.insert(reservationTimeRequest.getStartAt()));
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeRepository.findAllTimes().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteReservationTime(final Long id) {
        reservationTimeRepository.delete(id);
    }
}
