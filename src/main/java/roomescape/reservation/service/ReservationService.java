package roomescape.reservation.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.request.ReservationCreateRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeService reservationTimeService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return ReservationResponse.from(reservations);
    }

    public ReservationResponse create(ReservationCreateRequest request) {
        ReservationTime reservationTime = reservationTimeService.getReservationTime(request.timeId());
        Reservation reservation = Reservation.create(request.name(), request.date(), reservationTime);
        Reservation created = reservationRepository.save(reservation);

        return ReservationResponse.from(created);
    }

    public void deleteById(Long id) {
        Reservation reservation = getReservation(id);
        reservationRepository.deleteById(reservation.getId());
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 예약을 찾을 수 없습니다."));
    }
}
