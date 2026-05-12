package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.ThemeSlot;
import roomescape.domain.Time;
import roomescape.global.exception.CustomException;
import roomescape.global.exception.ErrorCode;
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
        validateIsExistBy(date, reservationTimeId, themeId);

        Theme theme = getThemeOrElseThrow(themeId);
        Time time = getTimeOrElseThrow(reservationTimeId);
        Reservation reservation = reservationRepository.save(new Reservation(name, date, time, theme));
        themeSlotRepository.update(new ThemeSlot(theme, date, time, true));
        return reservation;
    }

    private void validateIsExistBy(LocalDate date, Long reservationTimeId, Long themeId) {
        if (reservationRepository.isExistBy(themeId, date, reservationTimeId)) {
            throw new CustomException(ErrorCode.RESERVATION_ALREADY_EXIST);
        }
    }

    public void removeReservation(long reservationId) {
        getReservationOrElseThrow(reservationId);
        reservationRepository.deleteById(reservationId);
    }

    public Reservation findReservation(long reservationId) {
        return getReservationOrElseThrow(reservationId);
    }

    public List<Reservation> findReservationBy(String name) {
        return reservationRepository.findByName(name);
    }

    @NonNull
    private Theme getThemeOrElseThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));
    }

    @NonNull
    private Time getTimeOrElseThrow(Long reservationTimeId) {
        return timeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_NOT_FOUND));
    }

    @NonNull
    private Reservation getReservationOrElseThrow(long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }
}
