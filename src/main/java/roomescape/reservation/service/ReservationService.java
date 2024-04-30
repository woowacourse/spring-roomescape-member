package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("잘못된 예약 시간입니다. id=%d를 확인해주세요.", reservationRequest.timeId())
                ));

        Reservation reservation = new Reservation(
                reservationRequest.name(),
                reservationRequest.date(),
                reservationTime
        );
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    public void delete(long reservationId) {
        if (!reservationRepository.deleteById(reservationId)) {
            throw new IllegalArgumentException(String.format("잘못된 예약입니다. id=%d를 확인해주세요.", reservationId));
        }
    }
}
