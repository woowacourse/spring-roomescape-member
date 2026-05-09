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
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation save(final String name, final LocalDate date, final Long timeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(ReservationTimeNotFoundException::new);

        validateDateTime(date, reservationTime.getStartAt());
        existsByDateAndTimeId(date, timeId);

        Reservation reservation =
                Reservation.createNew(name, date, reservationTime);

        return reservationRepository.save(reservation);
    }

    private void validateDateTime(final LocalDate date, final LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new ReservationBadRequestException(
                    ReservationErrorCode.RESERVATION_INVALID_DATE.getMessage()
            );
        }
    }

    private void existsByDateAndTimeId(final LocalDate date, final Long timeId) {
        if (reservationRepository.existsByDateAndTimeId(date, timeId)) {
            throw new ReservationDuplicateException();
        }
    }

    @Transactional
    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }

}
