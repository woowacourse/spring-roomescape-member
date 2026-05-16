package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;

@Component
public class ReservationCreator {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationCreator(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = findReservationTime(timeId);
        validateAlreadyReserved(date, timeId, themeId);
        Theme theme = findTheme(themeId);
        Reservation reservation = new Reservation(null, name, date, time, theme);
        Long id = reservationRepository.insert(reservation);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("생성된 예약을 찾을 수 없습니다."));
    }

    public ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findBy(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
    }

    private void validateAlreadyReserved(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsWith(date, timeId, themeId)) {
            throw new DuplicateReservationException("이미 예약된 시간입니다.");
        }
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findBy(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
    }
}
