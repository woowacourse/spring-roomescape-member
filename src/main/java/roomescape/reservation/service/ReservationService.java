package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.doamin.Theme;
import roomescape.theme.service.ThemeService;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;


    public Reservation createReservation(String name,
                                  LocalDate date,
                                  long timeId,
                                  long themeId) {
        validateReservationDateNotPast(date);

        final ReservationTime findTime = reservationTimeService.getTime(timeId);
        validateReservationTimeNotPast(findTime);

        final Theme findTheme = themeService.getTheme(themeId);

        validateDuplicateReservation(date, timeId, themeId);

        return reservationRepository.save(Reservation.of(name, date, findTime, findTheme));
    }

    public void deleteReservation(long id) {
        reservationRepository.delete(id);
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

    private void validateReservationDateNotPast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("예약 날짜는 오늘 이후여야 합니다.");
        }
    }

    private void validateDuplicateReservation(LocalDate date, long timeId, long themeId) {
        reservationRepository.findByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .ifPresent(r -> {
                    throw new IllegalArgumentException("이미 예약이 존재하는 시간입니다.");
                });
    }

    private void validateReservationTimeNotPast(ReservationTime reservationTime) {
        if (reservationTime.getStartAt().isBefore(LocalDate.now().atStartOfDay().toLocalTime())) {
            throw new IllegalArgumentException("예약 시간은 현재 시간 이후여야 합니다.");
        }
    }
}
