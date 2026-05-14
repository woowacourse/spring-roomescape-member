package roomescape.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.ReservationAccessDeniedException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.exception.ReservationPastDateTimeException;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.payload.ReservationUpdateRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
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

    @Transactional
    public Reservation save(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(request.timeId()));
        validatePastReservation(request.date(), reservationTime.getStartAt());

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ThemeNotFoundException(request.themeId()));

        Reservation reservation = Reservation.create(
                request.name(),
                request.date(),
                reservationTime,
                theme
        );
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation update(Long id, ReservationUpdateRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        validatePastReservation(reservation.getDate(), reservation.getTime().getStartAt());

        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(request.timeId()));
        validatePastReservation(request.date(), reservationTime.getStartAt());

        return reservationRepository.update(
                reservation,
                request.date(),
                reservationTime
        );
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByName(String name) {
        return reservationRepository.findAllByName(name);
    }

    @Transactional
    public void deleteById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        validatePastReservation(reservation.getDate(), reservation.getTime().getStartAt());

        int affected = reservationRepository.deleteById(id);
        if (affected == 0) {
            throw new ReservationNotFoundException(id);
        }
    }

    @Transactional
    public void deleteByIdAndName(Long id, String name) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if (!reservation.getName().equals(name)) {
            throw new ReservationAccessDeniedException(id);
        }

        validatePastReservation(reservation.getDate(), reservation.getTime().getStartAt());

        int affected = reservationRepository.deleteById(id);
        if (affected == 0) {
            throw new ReservationNotFoundException(id);
        }
    }

    private void validatePastReservation(LocalDate requestDate, LocalTime requestTime) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime requestDateTime = LocalDateTime.of(requestDate, requestTime);

        if (requestDateTime.isBefore(now)) {
            throw new ReservationPastDateTimeException(now);
        }
    }

}
