package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        validateDuplicateReservation(date, timeId, themeId);
        ReservationTime time = findReservationTime(timeId);
        Theme theme = findTheme(themeId);
        Reservation reservation = new Reservation(name, date, time, theme);
        Long id = reservationRepository.insert(reservation);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 ID입니다."));
    }

    private void validateDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeAndTheme(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NoSuchElementException("존재하지 않는 예약입니다.");
        }
        reservationRepository.delete(id);
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findBy(timeId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 예약 시간입니다."));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findBy(themeId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테마입니다."));
    }
}
