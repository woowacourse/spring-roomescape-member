package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
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

    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        validateAlreadyReserved(date, timeId, themeId);
        ReservationTime time = findReservationTime(timeId);
        Theme theme = findTheme(themeId);
        Reservation reservation = new Reservation(null, name, date, time, theme);
        Long id = reservationRepository.insert(reservation);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 ID입니다."));
    }

    private void validateAlreadyReserved(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeAndTheme(date, timeId, themeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약된 시간입니다.");
        }
    }

    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 ID입니다.");
        }
        reservationRepository.delete(id);
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findBy(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findBy(themeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마입니다."));
    }
}
