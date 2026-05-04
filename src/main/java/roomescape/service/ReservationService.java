package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {
    public static final String INVALID_RESERVATION_ID = "요청한 예약을 찾을 수 없습니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeService reservationTimeService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation reserve(ReservationCreateRequest request) {
        ReservationTime reservationTime = reservationTimeService.find(request.getTimeId());

        Reservation reservation = Reservation.of(request.getName(), request.getDate(), reservationTime);

        return reservationRepository.save(reservation);
    }

    public void cancel(long reservationId) {
        reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException(
                INVALID_RESERVATION_ID));
        reservationRepository.deleteById(reservationId);
    }
}
