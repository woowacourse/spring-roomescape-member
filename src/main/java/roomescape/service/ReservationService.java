package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(String name, LocalDate date, Long reservationTimeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationTimeId);
        Reservation transientReservation = Reservation.transientOf(name, date, reservationTime);
        return reservationRepository.save(transientReservation);
    }

    public void removeReservation(long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public Reservation findReservation(long reservationId) {
        return reservationRepository.findById(reservationId);
    }
}
