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
import roomescape.reservationtime.exception.ReservationTimeDuplicateException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeService themeService;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository, ThemeService themeService) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeService = themeService;
    }

    @Transactional
    public ReservationTime save(final LocalTime startAt, final Long themeId) {
        Theme theme = themeService.getById(themeId);
        ReservationTime reservationTime = ReservationTime.createNew(startAt, theme);

        if (reservationTimeRepository.existsByStartAtAndThemeId(startAt, themeId)) {
            throw new ReservationTimeDuplicateException();
        }

        return reservationTimeRepository.save(reservationTime);
    }

    @Transactional
    public void deleteById(final long timeId) {
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
