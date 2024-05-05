package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationCommandRepository;
import roomescape.domain.ReservationFactory;
import roomescape.domain.ReservationQueryRepository;

@Service
public class ReservationService {
    private final ReservationFactory reservationFactory;
    private final ReservationCommandRepository reservationCommandRepository;
    private final ReservationQueryRepository reservationQueryRepository;

    public ReservationService(ReservationFactory reservationFactory,
                              ReservationCommandRepository reservationCommandRepository,
                              ReservationQueryRepository reservationQueryRepository) {
        this.reservationCommandRepository = reservationCommandRepository;
        this.reservationQueryRepository = reservationQueryRepository;
        this.reservationFactory = reservationFactory;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        Reservation reservation = reservationFactory.create(request);
        return ReservationResponse.from(reservationCommandRepository.create(reservation));
    }

    @Transactional
    public void deleteById(long id) {
        Reservation reservation = reservationQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 입니다."));
        reservationCommandRepository.deleteById(reservation.getId());
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationQueryRepository.findAll();
        return convertToReservationResponses(reservations);
    }

    private List<ReservationResponse> convertToReservationResponses(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
