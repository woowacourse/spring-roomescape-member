package roomescape.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.common.exception.DomainException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static roomescape.reservation.exeption.ReservationErrorCode.*;
import static roomescape.reservationtime.exeption.ReservationTimeErrorCode.*;
import static roomescape.theme.exception.ThemeErrorCode.*;

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
                .orElseThrow(() -> new DomainException(RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new DomainException(THEME_NOT_FOUND));

        Reservation reservation = new Reservation(guestName, date, time, theme);

        validateNotDuplicated(reservation);
        validateNotPast(reservation);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new DomainException(RESERVATION_NOT_FOUND);
        }
    }

    @Transactional
    public void deleteMine(Long id, String guestName) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new DomainException(RESERVATION_NOT_FOUND));

        validateAlreadyStarted(reservation);
        validateIsMyReservation(guestName, reservation);

        if(!reservationRepository.deleteById(id)) { // 위에서 NOT_FOUND를 검증하긴 하지만, 삭제 과정 중에 다른 사람이 변경할 수도 있기에 이중으로 검증
            throw new DomainException(RESERVATION_NOT_FOUND);
        }

    }

    @Transactional(readOnly = true)
    public List<Reservation> findByGuestName(String guestName) {
        return reservationRepository.findByGuestName(guestName);
    }

    @Transactional
    public Reservation editDateTime(Long reservationId, LocalDate date, Long timeId, String guestName) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DomainException(RESERVATION_NOT_FOUND));
        validateAlreadyStarted(reservation);
        validateIsMyReservation(guestName, reservation);

        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new DomainException(RESERVATION_TIME_NOT_FOUND));

        Reservation editedReservation =
                new Reservation(reservation.getId(), reservation.getGuestName(), date, reservationTime, reservation.getTheme());
        validateNotDuplicated(editedReservation);
        validateNotPast(editedReservation);

        if(!reservationRepository.updateDateAndTime(reservationId, date, timeId)) { // 위에서 NOT_FOUND를 검증하긴 하지만, 수정 과정 중에 다른 사람이 변경할 수도 있기에 이중으로 검증
            throw new DomainException(RESERVATION_NOT_FOUND);
        }

        // TODO: 이미 취소된 예약인지 검증(soft delete 있어야 가능)
        return editedReservation;
    }

    private void validateAlreadyStarted(Reservation reservation) {
        if (reservation.isPast(LocalDateTime.now(clock))) {
            throw new DomainException(CANNOT_EDIT_ALREADY_STARTED_RESERVATION);
        }
    }

    private static void validateIsMyReservation(String guestName, Reservation reservation) {
        if(!reservation.isSameGuest(guestName)) {
            throw new DomainException(CANNOT_EDIT_OTHER_GUEST_RESERVATION);
        }
    }

    private void validateNotPast(Reservation reservation) {
        if (reservation.isPast(LocalDateTime.now(clock))) {
            throw new DomainException(PAST_RESERVATION_NOT_ALLOWED);
        }
    }

    private void validateNotDuplicated(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        )) {
            throw new DomainException(RESERVATION_ALREADY_EXISTS);
        }
    }
}
