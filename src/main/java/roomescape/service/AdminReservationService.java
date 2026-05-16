package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationCreator reservationCreator;

    public AdminReservationService(ReservationRepository reservationRepository, ReservationCreator reservationCreator) {
        this.reservationRepository = reservationRepository;
        this.reservationCreator = reservationCreator;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        return reservationCreator.create(name, date, timeId, themeId);
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
