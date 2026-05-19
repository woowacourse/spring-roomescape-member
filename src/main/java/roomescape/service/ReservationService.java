package roomescape.service;

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
    public void updateCanceled(Long id, LocalDateTime now, ReservationCancelPolicy policy) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_NOT_FOUND));
        policy.validate(reservation, now);
        int archived = reservationRepository.relocateToCanceledReservation(id);
        if (archived == 0) {
            throw new NotFoundException(NotFoundCode.RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteById(id);
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
        ReservationTime updatedTime = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new NotFoundException(NotFoundCode.RESERVATION_TIME_NOT_FOUND));

        Reservation updated = new Reservation(
                reservation.id(),
                reservation.name(),
                command.date(),
                updatedTime,
                reservation.theme());
        updated.validateNow(now);
        reservationRepository.updateReservation(updated);
        return updated;
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

    private static void checkReservationDuplication(int reservationCount) {
        if (reservationCount > 0) {
            throw new ConflictException(ConflictCode.RESERVATION_DUPLICATED);
        }
    }
}
