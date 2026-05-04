package roomescape.reservations.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationException;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.reservations.entity.Reservation;
import roomescape.reservations.entity.ReservationTime;
import roomescape.reservations.entity.ReservationRepository;
import roomescape.reservations.entity.ReservationTimeRepository;
import roomescape.reservations.presentation.dto.ReservationRequest;
import roomescape.reservations.presentation.dto.ReservationResponse;

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
        validateSaveRequest(request);
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
        Reservation reservation = Reservation.of(
                null,
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

    // TODO: 검증 로직 위치 분리
    private void validateSaveRequest(ReservationRequest request) {
        if (request == null) {
            throw new ReservationException(ErrorCode.RESERVATION_REQUEST_NULL);
        }
        if (request.name() == null || request.name().trim().isBlank()) {
            throw new ReservationException(ErrorCode.RESERVATION_NAME_EMPTY);
        }
        if (request.date() == null) {
            throw new ReservationException(ErrorCode.RESERVATION_DATE_NULL);
        }
        if (request.timeId() == null) {
            throw new ReservationTimeException(ErrorCode.RESERVATION_TIME_ID_NULL);
        }
    }
}
