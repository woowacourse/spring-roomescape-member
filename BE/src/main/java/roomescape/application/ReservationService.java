package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;
import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ReservationException;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.global.exception.customException.ThemeException;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation save(String name, LocalDate date, Long timeId, Long themeId) {
        validateUniqueByDateAndTimeIdAndThemeId(date, timeId, themeId);
        Reservation reservation = Reservation.createWithNullId(
                name,
                date,
                findTargetTimeById(timeId),
                findTargetThemeById(themeId)
        );
        return reservationRepository.save(reservation);
    }

    private void validateUniqueByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ReservationException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    private Theme findTargetThemeById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeException(ErrorCode.THEME_NOT_FOUND));
    }

    private ReservationTime findTargetTimeById(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
