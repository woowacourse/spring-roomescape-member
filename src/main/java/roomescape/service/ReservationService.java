package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(String name, LocalDate date, Long reservationTimeId, Long themeId) {
        Time time = getTimeOrElseThrow(reservationTimeId);
        Theme theme = getThemeOrElseThrow(themeId);
        Reservation transientReservation = Reservation.transientOf(name, date, time, theme);
        return reservationRepository.save(transientReservation);
    }

    public void removeReservation(long reservationId) {
        getReservationOrElseThrow(reservationId);
        reservationRepository.deleteById(reservationId);
    }

    public Reservation findReservation(long reservationId) {
        return getReservationOrElseThrow(reservationId);
    }

    @NonNull
    private Theme getThemeOrElseThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마 id가 존재하지 않습니다."));
    }

    @NonNull
    private Time getTimeOrElseThrow(Long reservationTimeId) {
        return timeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new IllegalArgumentException("시간 id가 존재하지 않습니다."));
    }

    @NonNull
    private Reservation getReservationOrElseThrow(long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 id가 존재하지 않습니다."));
    }
}
