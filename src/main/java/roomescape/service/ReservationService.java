package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.ThemeSlot;
import roomescape.domain.Time;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.ThemeSlotRepository;
import roomescape.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ThemeSlotRepository themeSlotRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            TimeRepository timeRepository,
            ThemeRepository themeRepository,
            ThemeSlotRepository themeSlotRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.themeSlotRepository = themeSlotRepository;
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation saveReservation(String name, LocalDate date, Long reservationTimeId, Long themeId) {
        Theme theme = getThemeOrElseThrow(themeId);
        Time time = getTimeOrElseThrow(reservationTimeId);

        // TODO 해당 테마, 날짜, 시간의 예약이 있는지 검사

        Reservation reservation = reservationRepository.save(new Reservation(name, date, time, theme));
        themeSlotRepository.update(new ThemeSlot(theme, date, time, true));
        return reservation;
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
