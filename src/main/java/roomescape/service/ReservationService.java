package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public boolean hasReservationsByTimeId(Long timeId) {
        return reservationRepository.existsByTimeId(timeId);
    }

    @Transactional
    public Reservation addReservation(Reservation reservation) {
        Long id = reservationRepository.save(reservation);

        return findById(id);
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 ID입니다."));
    }

    public boolean hasReservationsByThemeId(Long themeId) {
        return reservationRepository.existsByThemeId(themeId);
    }

    public boolean hasReservationsBy(LocalDate date, Long timeId, Long themeId) {
        return reservationRepository.existsBy(date, timeId, themeId);
    }

    public Set<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationRepository.findReservedTimeIdsByDateAndThemeId(date, themeId);
    }
}
