package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.InvalidReservationTimeException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAllWithTime();
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationTimeException(timeId));
        Theme theme = themeRepository.findById(themeId);
        if (reservationRepository.existsByTimeIdAndThemeId(timeId, themeId)) {
            throw new DuplicateReservationException("이미 존재하는 예약입니다.");
        }
        Reservation reservation = new Reservation(null, name, date, time, theme);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (reservationRepository.deleteById(id) == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
