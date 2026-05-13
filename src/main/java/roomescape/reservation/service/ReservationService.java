package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.ReservationBadRequestException;
import roomescape.reservation.exception.ReservationDuplicateException;
import roomescape.reservation.exception.ReservationErrorCode;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationResult;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public List<ReservationResult> getAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResult::from)
                .toList();
    }

    public List<ReservationResult> getAllByName(final String name) {
        return reservationRepository.findAllByName(name).stream()
                .map(ReservationResult::from)
                .toList();
    }

    @Transactional
    public ReservationResult save(final String name, final LocalDate date, final Long timeId) {
        ReservationTime reservationTime = findReservationTime(timeId);

        validateDuplicate(date, timeId);

        Reservation reservation =
                Reservation.createNew(name, date, reservationTime);

        validateDateTime(reservation, reservationTime.getStartAt());

        Reservation savedReservation =
                reservationRepository.save(reservation);

        return ReservationResult.from(savedReservation);
    }

    @Transactional
    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteById(final long id, final String name) {
        Reservation reservation = findReservation(id);

        validateOwner(name, reservation);

        reservationRepository.deleteById(id);
    }

    @Transactional
    public void update(final long id, final String name, final LocalDate date, final Long timeId) {
        Reservation reservation = findReservation(id);
        validateOwner(name, reservation);

        ReservationTime reservationTime = findReservationTime(timeId);
        validateDuplicate(date, timeId);

        reservation = reservation.modify(date, reservationTime);
        validateDateTime(reservation, reservationTime.getStartAt());

        reservationRepository.update(reservation);
    }

    private Reservation findReservation(final long id) {
        return reservationRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);
    }

    private ReservationTime findReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(ReservationTimeNotFoundException::new);
    }

    private void validateDuplicate(final LocalDate date, final Long timeId) {
        if (reservationRepository.existsByDateAndTimeId(date, timeId)) {
            throw new ReservationDuplicateException();
        }
    }

    private void validateDateTime(final Reservation reservation, final LocalTime time) {
        if (reservation.isPastTime(time, LocalDateTime.now())) {
             throw new ReservationBadRequestException(
                    ReservationErrorCode.RESERVATION_PAST_DATE.getMessage()
            );
        }
    }

    private void validateOwner(final String name, final Reservation reservation) {
        if (!reservation.getName().equals(name)) {
            throw new ReservationBadRequestException(
                    ReservationErrorCode.RESERVATION_NAME_MISMATCH.getMessage()
            );
        }
    }

}
