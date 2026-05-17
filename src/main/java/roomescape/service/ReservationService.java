package roomescape.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.command.ReservationEditCommand;
import roomescape.command.ReservationSaveCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnprocessableException;
import roomescape.exception.code.ConflictCode;
import roomescape.exception.code.NotFoundCode;
import roomescape.exception.code.UnprocessableCode;
import roomescape.policy.ReservationCancelPolicy;
import roomescape.policy.ReservationSavePolicy;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public void updateCancelled(Long id, LocalDateTime now, ReservationCancelPolicy policy) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_NOT_FOUND));
            policy.validate(reservation, now);
            reservationRepository.updateCancelled(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(NotFoundCode.RESERVATION_NOT_FOUND);
        }
    }

    @NonNull
    private Reservation getValidReservation(Long id, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_NOT_FOUND));
        if (reservation.isDateTimeBefore(now)) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_ALREADY_STARTED);
        }
        return reservation;
    }

    @Transactional
    public Reservation saveReservation(ReservationSaveCommand command, LocalDateTime now, ReservationSavePolicy policy) {
        ReservationTime reservationTime = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new NotFoundException(NotFoundCode.THEME_NOT_FOUND));
        Reservation reservation = Reservation.forSave(command, reservationTime, theme);
        policy.validate(reservation, now);

        return reservationRepository.addReservation(reservation);
    }

    public List<Reservation> findReservationsByName(String name) {
        return reservationRepository.findReservationsByName(name);
    }

    @Transactional
    public Reservation editReservation(Long id, ReservationEditCommand command, LocalDateTime now) {
        Reservation reservation = getValidReservation(id, now);
        int reservationCount = reservationRepository.countReservationsOf(command.date(), command.timeId(),
                reservation.themeId());
        checkReservationDuplication(reservationCount);
        validateEditedDateTime(command, now);

        try {
            reservationRepository.updateReservation(id, command);
            return reservationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_NOT_FOUND));
        } catch (IllegalStateException e) {
            throw new NotFoundException(NotFoundCode.RESERVATION_TIME_NOT_FOUND);
        }
    }

    private void validateEditedDateTime(ReservationEditCommand command, LocalDateTime now) {
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_TIME_NOT_FOUND));
        LocalDateTime editedDateTime = command.date().atTime(time.startAt());

        if (command.date().isBefore(now.toLocalDate())) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_PAST_DATE);
        }
        if (editedDateTime.isBefore(now)) {
            throw new UnprocessableException(UnprocessableCode.RESERVATION_PAST_TIME);
        }
    }

    private static void checkReservationDuplication(int reservationCount) {
        if (reservationCount > 0) {
            throw new ConflictException(ConflictCode.RESERVATION_DUPLICATED);
        }
    }
}
