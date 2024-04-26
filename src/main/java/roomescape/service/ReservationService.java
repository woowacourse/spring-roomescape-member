package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.app.ReservationAppRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }
    
    public Reservation save(ReservationAppRequest request) {
        Reservation newReservation = new Reservation(request.name(), request.date(),
                reservationTimeRepository.findById(request.timeId()));

        return reservationRepository.save(newReservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
