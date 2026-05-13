package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeConstraintException;
import roomescape.reservationtime.exception.ReservationTimeDuplicateException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.reservationtime.service.dto.ReservationTimeResult;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationTimeResult save(final LocalTime startAt, final Long themeId) {
        validateDuplicate(startAt, themeId);

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(ThemeNotFoundException::new);

        ReservationTime reservationTime = ReservationTime.createNew(startAt, theme);

        ReservationTime savedReservationTime =
                reservationTimeRepository.save(reservationTime);

        return ReservationTimeResult.from(savedReservationTime);
    }

    @Transactional
    public void deleteById(final long timeId) {
        validateReservationExists(timeId);
        reservationTimeRepository.deleteById(timeId);
    }

    public List<ReservationTimeResult> findAllByThemeId(final long themeId) {
        return reservationTimeRepository.findAllByThemeId(themeId).stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public List<ReservationTimeResult> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public List<ReservationTimeResult> findAvailableTimes(final LocalDate date, final long themeId) {
        return reservationTimeRepository.findAvailableTimes(date, themeId).stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    private void validateDuplicate(final LocalTime startAt, final Long themeId) {
        if (reservationTimeRepository.existsByStartAtAndThemeId(startAt, themeId)) {
            throw new ReservationTimeDuplicateException();
        }
    }

    private void validateReservationExists(final long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new ReservationTimeConstraintException();
        }
    }
}
