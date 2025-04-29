package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.repository.EntityRepository;

@Service
public class ReservationService {

    private final EntityRepository<Reservation> reservationRepository;
    private final EntityRepository<ReservationTime> reservationTimeRepository;

    public ReservationService(EntityRepository<Reservation> reservationRepository,
                              EntityRepository<ReservationTime> reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationRequest request) {
        Long timeId = request.timeId();
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("reservationsTime not found id =" + timeId));

        Reservation reservation = Reservation.withoutId(request.name(), request.date(), reservationTime);

        Reservation saved = reservationRepository.save(reservation);

        return ReservationResponse.from(saved);
    }

    public void delete(Long id){
        reservationRepository.deleteById(id);
    }
}
