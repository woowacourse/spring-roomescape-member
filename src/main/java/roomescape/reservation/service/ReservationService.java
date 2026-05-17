package roomescape.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AccessDeniedException;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.PastDateTimeException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.payload.ReservationUpdateRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.entity.Theme;
import roomescape.theme.service.ThemeService;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.clock = clock;
    }

    @Transactional
    public Reservation save(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeService.getById(request.timeId());
        validatePastReservation(request.date(), reservationTime.getStartAt());

        Theme theme = themeService.getById(request.themeId());

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
        Reservation reservation = getById(id);
        validatePastReservation(reservation.getDate(), reservation.getTime().getStartAt());

        ReservationTime reservationTime = reservationTimeService.getById(request.timeId());
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
        Reservation reservation = getById(id);
        validatePastReservation(reservation.getDate(), reservation.getTime().getStartAt());

        int affected = reservationRepository.deleteById(id);
        if (affected == 0) {
            throw new NotFoundException(DomainType.RESERVATION, id);
        }
    }

    @Transactional
    public void deleteByIdAndName(Long id, String name) {
        Reservation reservation = getById(id);
        if (!reservation.isOwner(name)) {
            throw new AccessDeniedException(DomainType.RESERVATION, id);
        }

        validatePastReservation(reservation.getDate(), reservation.getTime().getStartAt());

        int affected = reservationRepository.deleteById(id);
        if (affected == 0) {
            throw new NotFoundException(DomainType.RESERVATION, id);
        }
    }

    private Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DomainType.RESERVATION, id));
    }

    private void validatePastReservation(LocalDate requestDate, LocalTime requestTime) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime requestDateTime = LocalDateTime.of(requestDate, requestTime);

        if (requestDateTime.isBefore(now)) {
            throw new PastDateTimeException(DomainType.RESERVATION, now, requestDateTime);
        }
    }

}
