package roomescape.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.app.ReservationAppRequest;
import roomescape.exception.reservation.DuplicatedReservationException;
import roomescape.exception.reservation.IllegalDateFormatException;
import roomescape.exception.reservation.PastReservationException;
import roomescape.exception.reservation.ReservationTimeNotFoundException;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Reservation save(ReservationAppRequest request) {
        LocalDate date = parseDate(request.date());
        ReservationTime time = findTime(request.timeId());
        Reservation reservation = new Reservation(request.name(), date, time);
        validatePastReservation(date, time);
        validateDuplication(date, request.timeId());

        return reservationRepository.save(reservation);
    }

    private LocalDate parseDate(String rawDate) {
        try {
            return LocalDate.parse(rawDate);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalDateFormatException();
        }
    }

    private ReservationTime findTime(Long timeId) {
        if (timeId == null) {
            throw new ReservationTimeNotFoundException();
        }
        try {
            return reservationTimeRepository.findById(timeId);
        } catch (EmptyResultDataAccessException e) {
            throw new ReservationTimeNotFoundException();
        }
    }

    private void validatePastReservation(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new PastReservationException();
        }
        if (date.isEqual(LocalDate.now()) && time.isBeforeNow()) {
            throw new PastReservationException();
        }
    }

    private void validateDuplication(LocalDate date, Long timeId) {
        long dataCount = reservationRepository.countByDateAndTimeId(date, timeId);
        if (dataCount > 0) {
            throw new DuplicatedReservationException();
        }
    }

    public int delete(Long id) {
        return reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
