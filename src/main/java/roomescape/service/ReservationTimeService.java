package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeAvailability;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeService {
    private static final String RESERVATION_TIME_ALREADY_EXISTS_MESSAGE = "이미 존재하는 예약 시간입니다.";
    private static final String THEME_NOT_FOUND_MESSAGE = "존재하지 않는 테마입니다.";

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime create(LocalTime startAt) {
        ReservationTime reservationTime = new ReservationTime(startAt);
        validateNotDuplicated(reservationTime);

        return reservationTimeRepository.save(reservationTime);
    }

    private void validateNotDuplicated(ReservationTime reservationTime) {
        if (reservationTimeRepository.existsByStartAt(reservationTime.getStartAt())) {
            throw new InvalidRequestException(RESERVATION_TIME_ALREADY_EXISTS_MESSAGE);
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeAvailability> findAvailableTimes(LocalDate date, Long themeId) {
        validateThemeExists(themeId);

        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(reservationTime -> new ReservationTimeAvailability(
                        reservationTime,
                        reservations.stream()
                                .noneMatch(reservation -> reservation.isSameTime(reservationTime))
                ))
                .toList();
    }

    private void validateThemeExists(Long themeId) {
        if (!themeRepository.existsById(themeId)) {
            throw new NotFoundException(THEME_NOT_FOUND_MESSAGE);
        }
    }
}
