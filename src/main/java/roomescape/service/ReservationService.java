package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        Theme theme = themeRepository.findById(request.themeId());
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        Reservation newReservation = new Reservation(null, request.name(), theme, request.date(), reservationTime);

        Long newReservationId = reservationRepository.save(newReservation);
        return reservationRepository.findById(newReservationId);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
