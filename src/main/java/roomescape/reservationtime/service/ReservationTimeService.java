package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeConstraintException;
import roomescape.reservationtime.exception.ReservationTimeDuplicateException;
import roomescape.reservationtime.exception.ReservationTimeErrorCode;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationTime save(final LocalTime startAt, final Long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(ThemeNotFoundException::new);
        ReservationTime reservationTime = ReservationTime.createNew(startAt, theme);

        if (reservationTimeRepository.existsByStartAtAndThemeId(startAt, themeId)) {
            throw new ReservationTimeDuplicateException();
        }

        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional
    public void deleteById(final long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new ReservationTimeConstraintException(
                    ReservationTimeErrorCode.RESERVATION_TIME_CONSTRAINT.getMessage()
            );
        }
        reservationTimeRepository.deleteById(timeId);
    }

    public List<ReservationTime> findAllByThemeId(final long themeId) {
        return reservationTimeRepository.findAllByThemeId(themeId);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> findAvailableTimes(final LocalDate date, final long themeId) {
        List<ReservationTime> reservationTimes =
                reservationTimeRepository.findAllByThemeId(themeId);

        Set<Long> reservedTimeIds = new HashSet<>(
                reservationRepository.findAllByDateAndThemeId(date, themeId)
        );

        return reservationTimes.stream()
                .filter(time -> !reservedTimeIds.contains(time.getId()))
                .toList();
    }

}
