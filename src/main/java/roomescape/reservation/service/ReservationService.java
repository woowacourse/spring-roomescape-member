package roomescape.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.util.DateTime;
import roomescape.common.util.SystemDateTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;

@Service
public class ReservationService {

    private final DateTime dateTime;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(
            DateTime dateTime,
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.dateTime = dateTime;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId());

        LocalDateTime now = dateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(request.date(), time.getStartAt());

        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("예약할 수 없는 시간입니다.");
        }

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
