package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationException;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationResponse saveReservation(ReservationRequest request) {
        if (request == null) {
            throw new ReservationException(ErrorCode.RESERVATION_REQUEST_NULL);
        }
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
        Reservation reservation = Reservation.createWithNullId(
                request.name(),
                request.date(),
                time
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteReservation(Long id) {
        if (id == null) {
            throw new ReservationException(ErrorCode.RESERVATION_ID_NULL);
        }
        reservationRepository.deleteById(id);
    }
}
