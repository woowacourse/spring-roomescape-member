package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.reservation.ReservationAlreadyExistsException;
import roomescape.exception.reservation.ReservationNotFoundException;
import roomescape.repository.reservation.ReservationRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeService timeService;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeService timeService) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
    }

    public ReservationResponse create(ReservationRequest request) {
        ReservationTime time = timeService.getById(request.timeId());
        Reservation newReservation = new Reservation(request.name(), request.date(), time);
        if (reservationRepository.existsByDateAndTime(request.date(), time.getId())) {
            throw new ReservationAlreadyExistsException();
        }

        return ReservationResponse.from(reservationRepository.add(newReservation));
    }

    public List<ReservationResponse> getAll() {
        return ReservationResponse.from(reservationRepository.findAll());
    }

    public void deleteById(Long id) {
        int affectedCount = reservationRepository.deleteById(id);
        if (affectedCount == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
