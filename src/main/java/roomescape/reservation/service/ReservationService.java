package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public long createReservation(String name,
                                  LocalDate date,
                                  long timeId,
                                  long themeId) {
        Optional<ReservationTime> findTime = reservationTimeRepository.findById(timeId);
        if (findTime.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }

        Optional<Theme> findTheme = themeRepository.findById(themeId);
        if (findTheme.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }

        return reservationRepository.save(Reservation.of(name, date, findTime.get(), findTheme.get()));
    }

    public void deleteReservation(long id) {
        reservationRepository.remove(id);
    }

    public List<Reservation> getReservationsByUsername(String username) {
        return reservationRepository.findAllByName(username);
    }

    public Reservation getReservation(long id) {
        Optional<Reservation> findReservation = reservationRepository.findById(id);
        if (findReservation.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }

        return findReservation.get();
    }
}
