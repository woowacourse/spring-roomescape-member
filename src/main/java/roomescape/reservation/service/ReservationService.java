package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<Reservation> findReservations(int page, int size) {
        return reservationRepository.findAll(page, size);
    }


    public List<Reservation> findUserReservations(String name, int page, int size) {
        return reservationRepository.findByName(name, page, size);
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND));

        validateNotPast(date, time.getStartAt());
        validateUnique(date, timeId, themeId);

        Reservation reservation = new Reservation(null, name, date, time, theme);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation updateReservation(Long id, String name, LocalDate date, Long timeId) {
        Reservation reservation = reservationRepository.findByIdAndName(id, name)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        validateExistingNotPast(reservation, ErrorCode.PAST_RESERVATION_UPDATE);
        validateNotPast(date, time.getStartAt());
        validateUniqueForUpdate(id, date, timeId, reservation.getTheme().getId());

        if (reservationRepository.updateDateAndTimeByIdAndName(id, name, date, timeId) == 0) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return new Reservation(id, name, date, time, reservation.getTheme());
    }
    @Transactional
    public void deleteUserReservation(Long id, String name) {
        reservationRepository.findByIdAndName(id, name)
                .ifPresent(reservation -> validateExistingNotPast(reservation, ErrorCode.PAST_RESERVATION_DELETE));
        reservationRepository.deleteByIdAndName(id, name);
    }

    private void validateUnique(LocalDate date, Long timeId, Long themeId) {
        if(reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESERVATION);
        }
    }

    private void validateNotPast(LocalDate date, LocalTime time) {
        if(LocalDateTime.of(date, time).isBefore(LocalDateTime.now(clock))) {
            throw new BusinessException(ErrorCode.PAST_RESERVATION);
        }
    }

    private void validateExistingNotPast(Reservation reservation, ErrorCode errorCode) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        if (reservationDateTime.isBefore(LocalDateTime.now(clock))) {
            throw new BusinessException(errorCode);
        }
    }

    private void validateUniqueForUpdate(Long id, LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeIdAndIdNot(id, date, timeId, themeId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESERVATION);
        }
    }
}
