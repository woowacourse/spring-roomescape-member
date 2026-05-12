package roomescape.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.common.exception.DomainException;
import roomescape.common.exception.ErrorCode;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    private final Clock clock;

    @Transactional(readOnly = true)
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation create(String guestName, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new DomainException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new DomainException(ErrorCode.THEME_NOT_FOUND));

        Reservation reservation = new Reservation(guestName, date, time, theme);

        validateNotDuplicated(reservation);
        validateNotPast(reservation);

        return reservationRepository.save(reservation);
    }

    private void validateNotPast(Reservation reservation) {
        if (reservation.isPast(LocalDateTime.now(clock))) {
            throw new DomainException(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
        }
    }

    private void validateNotDuplicated(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        )) {
            throw new DomainException(ErrorCode.RESERVATION_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new DomainException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    public List<Reservation> findByGuestName(String guestName) {
        return reservationRepository.findByGuestName(guestName);
    }
}
