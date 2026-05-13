package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.NotFoundException;
import roomescape.exception.PastReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.result.TimeAvailabilityResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation createUserReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = findReservationTime(timeId);
        validateNotPast(date, time);
        return create(name, date, timeId, themeId, time);
    }

    @Transactional
    public Reservation createAdminReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = findReservationTime(timeId);
        return create(name, date, timeId, themeId, time);
    }

    @Transactional
    public void delete(Long id) {
        validateId(id);
        reservationRepository.delete(id);
    }

    public List<TimeAvailabilityResult> findAvailableTime(Long themeId, LocalDate date) {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findReservationsByThemeAndDate(themeId, date);

        return times.stream()
                .map(time -> new TimeAvailabilityResult(
                        time.getId(),
                        time.getStartAt(),
                        isAvailable(time, reservations)
                ))
                .toList();
    }

    private Reservation create(String name, LocalDate date, Long timeId, Long themeId, ReservationTime time) {
        validateAlreadyReserved(date, timeId, themeId);
        Theme theme = findTheme(themeId);
        Reservation reservation = new Reservation(null, name, date, time, theme);
        Long id = reservationRepository.insert(reservation);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 ID입니다."));
    }

    private void validateAlreadyReserved(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existWith(date, timeId, themeId)) {
            throw new DuplicateReservationException("이미 예약된 시간입니다.");
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PastReservationException("이미 지난 시간으로는 예약할 수 없습니다.");
        }
    }

    private boolean isAvailable(ReservationTime time, List<Reservation> reservations) {
        return reservations.stream()
                .noneMatch(reservation -> time.equals(reservation.getTime()));
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findBy(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findBy(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("[ERROR] id는 양수이어야 합니다.");
        }
    }
}
