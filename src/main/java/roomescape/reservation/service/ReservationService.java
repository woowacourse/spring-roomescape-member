package roomescape.reservation.service;

import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_ALREADY_RESERVED;
import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_DATE_IN_PAST;
import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_NOT_CHANGED;
import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_TIME_IN_PAST;
import static roomescape.exception.code.RoomEscapeErrorCode.RESERVATION_TIME_NOT_FOUND;
import static roomescape.exception.code.RoomEscapeErrorCode.THEME_NOT_FOUND;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.service.exception.ReservationChangeException;
import roomescape.reservation.service.exception.ReservationCreateException;
import roomescape.reservation.service.exception.ReservationNotFoundException;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;


    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation createReservation(String name,
                                  LocalDate date,
                                  long timeId,
                                  long themeId) {
        validateCreateReservationDateNotPast(date);

        Optional<ReservationTime> findTime = reservationTimeRepository.findById(timeId);
        validateCreateReservationTime(findTime);

        Optional<Theme> findTheme = themeRepository.findById(themeId);
        validateTheme(findTheme);
        validateCreateDuplicateReservation(date, timeId, themeId);

        return reservationRepository.save(Reservation.of(name, date, findTime.get(), findTheme.get()));
    }

    public void deleteReservation(long id) {
        reservationRepository.delete(id);
    }

    public void deleteMyReservation(long id, String username) {
        if (!isMyReservation(getReservation(id), username)) {
            throw new ReservationNotFoundException();
        }

        reservationRepository.delete(id);
    }

    public Reservation changeMyReservationDateTime(
            long id,
            String username,
            LocalDate date,
            long timeId
    ) {
        Reservation reservation = getReservation(id);
        if (!isMyReservation(reservation, username)) {
            throw new ReservationNotFoundException();
        }

        validateReservationChanged(reservation, date, timeId);
        validateChangeReservationDateNotPast(date);

        Optional<ReservationTime> findTime = reservationTimeRepository.findById(timeId);
        validateChangeReservationTime(findTime);
        validateChangeDuplicateReservation(date, timeId, reservation.getTheme().getId());

        reservation.changeDateTime(date, findTime.get());

        return reservationRepository.save(reservation);
    }

    private boolean isMyReservation(Reservation reservation, String username) {
        return reservation.getName().equals(username);
    }

    public List<Reservation> getMyReservations(String username) {
        return reservationRepository.findAllByName(username);
    }

    public Reservation getReservation(long id) {
        Optional<Reservation> findReservation = reservationRepository.findById(id);
        if (findReservation.isEmpty()) {
            throw new ReservationNotFoundException();
        }

        return findReservation.get();
    }

    private void validateCreateReservationDateNotPast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new ReservationCreateException(RESERVATION_DATE_IN_PAST);
        }
    }

    private void validateCreateReservationTime(Optional<ReservationTime> findTime) {
        if (findTime.isEmpty()) {
            throw new ReservationCreateException(RESERVATION_TIME_NOT_FOUND);
        }

        if (findTime.get().getStartAt().isBefore(LocalDate.now().atStartOfDay().toLocalTime())) {
            throw new ReservationCreateException(RESERVATION_TIME_IN_PAST);
        }
    }

    private void validateTheme(Optional<Theme> findTheme) {
        if (findTheme.isEmpty()) {
            throw new ReservationCreateException(THEME_NOT_FOUND);
        }
    }

    private void validateCreateDuplicateReservation(LocalDate date, long timeId, long themeId) {
        reservationRepository.findByThemeIdAndDateAndTimeId(date, timeId, themeId)
                .ifPresent(reservation -> {
                    throw new ReservationCreateException(RESERVATION_ALREADY_RESERVED);
                });
    }

    private void validateReservationChanged(Reservation reservation, LocalDate date, long timeId) {
        if (reservation.getDate().equals(date) && reservation.getTime().getId().equals(timeId)) {
            throw new ReservationChangeException(RESERVATION_NOT_CHANGED);
        }
    }

    private void validateChangeReservationDateNotPast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new ReservationChangeException(RESERVATION_DATE_IN_PAST);
        }
    }

    private void validateChangeReservationTime(Optional<ReservationTime> findTime) {
        if (findTime.isEmpty()) {
            throw new ReservationChangeException(RESERVATION_TIME_NOT_FOUND);
        }

        if (findTime.get().getStartAt().isBefore(LocalDate.now().atStartOfDay().toLocalTime())) {
            throw new ReservationChangeException(RESERVATION_TIME_IN_PAST);
        }
    }

    private void validateChangeDuplicateReservation(LocalDate date, long timeId, long themeId) {
        reservationRepository.findByThemeIdAndDateAndTimeId(date, timeId, themeId)
                .ifPresent(reservation -> {
                    throw new ReservationChangeException(RESERVATION_ALREADY_RESERVED);
                });
    }

}
