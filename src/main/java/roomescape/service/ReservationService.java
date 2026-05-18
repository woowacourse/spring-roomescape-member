package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ReservationSaveCommand;
import roomescape.service.command.ReservationUpdateCommand;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAllReservations();
    }

    public Reservation saveReservation(ReservationSaveCommand command) {
        ReservationTime reservationTime = findReservationTime(command.timeId());
        validateReservableDateTime(command.date(), reservationTime.startAt());

        Theme theme = findTheme(command);
        validateDuplicatedReservation(command.date(), command.timeId(), command.themeId());

        Reservation reservation = new Reservation(null, command.name(), command.date(), reservationTime, theme);

        return reservationRepository.addReservation(reservation);
    }

    public List<Reservation> findReservationsByName(String name) {
        if (Objects.isNull(name)) {
            throw new RoomescapeException(ErrorCode.INVALID_INPUT);
        }

        List<Reservation> reservations = reservationRepository.findReservationsByName(name);

        if (reservations.isEmpty()) {
            throw new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        return reservations;
    }

    public void deleteReservationByAdmin(Long id) {
        Reservation reservation = findReservation(id);

        reservationRepository.deleteById(reservation.id());
    }

    public void cancelReservation(Long id) {
        Reservation reservation = findReservation(id);

        validateNotPastReservation(reservation);
        reservationRepository.deleteById(reservation.id());
    }

    public Reservation changeReservationDateTime(Long id, ReservationUpdateCommand command) {
        Reservation reservation = findReservation(id);
        validateNotPastReservation(reservation);

        ReservationTime reservationTime = findReservationTime(command.timeId());

        validateReservableDateTime(command.date(), reservationTime.startAt());
        validateConflictingReservation(command.date(), command.timeId(), reservation.themeId(),
                reservation.id());

        reservationRepository.updateDateTime(reservation.id(), command.date(), command.timeId());

        return new Reservation(
                reservation.id(),
                reservation.name(),
                command.date(),
                reservationTime,
                reservation.theme()
        );
    }

    private void validateReservableDateTime(LocalDate date, LocalTime startAt) {
        if (date == null || startAt == null) {
            throw new RoomescapeException(ErrorCode.INVALID_INPUT);
        }

        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_PAST_TIME);
        }
    }

    private void validateDuplicatedReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme findTheme(ReservationSaveCommand command) {
        return themeRepository.findById(command.themeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND));
    }

    private Reservation findReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    private void validateNotPastReservation(Reservation reservation) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.date(), reservation.time().startAt());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_ALREADY_PAST);
        }
    }

    private void validateConflictingReservation(LocalDate date, Long timeId, Long themeId, Long reservationId) {
        if (reservationRepository.existsConflictingReservation(date, timeId, themeId, reservationId)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATED);
        }
    }
}
