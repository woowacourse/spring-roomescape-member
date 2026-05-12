package roomescape.application;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.global.exception.customException.ConflictException;
import roomescape.global.exception.customException.DomainRuleViolationException;
import roomescape.global.exception.customException.NotFoundException;

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
        validateFutureOrPresentDate(date);
        validateFutureOrPresentTime(timeId);
        validateUniquenessByDateAndTimeIdAndThemeId(date, timeId, themeId);
        Reservation reservation = Reservation.createWithNullId(
                name,
                date,
                findTargetTimeById(timeId),
                findTargetThemeById(themeId)
        );
        return reservationRepository.save(reservation);
    }

    private void validateFutureOrPresentDate(LocalDate date) {
        LocalDate now = LocalDate.now();
        if (date.isBefore(now)) {
            throw new DomainRuleViolationException(ErrorCode.PAST_DATE_OR_TIME);
        }
    }

    private void validateFutureOrPresentTime(Long id) {
        ReservationTime reservationTime = findTargetTimeById(id);
        LocalTime requestTimeValue = reservationTime.startAt();
        LocalTime nowTime = LocalTime.now();
        if (requestTimeValue.isBefore(nowTime)) {
            throw new DomainRuleViolationException(ErrorCode.PAST_DATE_OR_TIME);
        }
    }

    private void validateUniquenessByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ConflictException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    private Theme findTargetThemeById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }

    private ReservationTime findTargetTimeById(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByName(String name) {
        return reservationRepository.findByName(name);
    }

    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
