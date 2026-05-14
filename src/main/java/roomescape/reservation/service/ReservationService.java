package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

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

    public List<Reservation> getReservations() {
        return reservationRepository.findAllWithTime();
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND));
        validatePastDateTime(date, time);
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new BusinessException(ErrorCode.RESERVATION_DUPLICATE);
        }
        Reservation reservation = new Reservation(null, name, date, time, theme);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteById(id);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public List<Reservation> getUserReservations(String name) {
        return reservationRepository.findByName(name);
    }

    @Transactional
    public void cancelUserReservation(Long id, String name) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
        if (!reservation.isOwnedBy(name)) {
            throw new BusinessException(ErrorCode.RESERVATION_FORBIDDEN);
        }
        if (reservation.isExpired(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.RESERVATION_EXPIRED);
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public Reservation updateUserReservation(Long id, String name, LocalDate date, Long timeId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.isOwnedBy(name)) {
            throw new BusinessException(ErrorCode.RESERVATION_FORBIDDEN);
        }

        if (reservation.isExpired(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.RESERVATION_EXPIRED);
        }

        ReservationTime newTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        validatePastDateTime(date, newTime);

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, reservation.getTheme().getId())) {
            throw new BusinessException(ErrorCode.RESERVATION_DUPLICATE);
        }

        reservationRepository.update(id, date, timeId);
        return new Reservation(id, name, date, newTime, reservation.getTheme());
    }

    private void validatePastDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime request = LocalDateTime.of(date, time.getStartAt());
        if (request.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.RESERVATION_PAST_DATETIME);
        }
    }
}
