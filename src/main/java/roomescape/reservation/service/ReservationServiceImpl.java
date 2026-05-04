package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.InvalidReservationTimeException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAllWithTime();
    }

    public Reservation createReservation(String name, LocalDate date, Long timeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationTimeException(timeId));
        Long id = reservationRepository.save(name, date, timeId);
        return new Reservation(id, name, date, time);
    }

    public void deleteReservation(Long id) {
        if (reservationRepository.deleteById(id) == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
