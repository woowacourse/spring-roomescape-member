package roomescape.reservation.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId());

        Long id = reservationRepository.save(new Reservation(null, request.name(), request.date(), time));
        Reservation findReservation = reservationRepository.findById(id);

        return ReservationResponse.from(findReservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteReservationById(final Long id) {
        int count = reservationRepository.deleteById(id);
        validateExistIdToDelete(count);
    }

    private void validateExistIdToDelete(int count) {
        if (count == 0) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 예약입니다.");
        }
    }
}
