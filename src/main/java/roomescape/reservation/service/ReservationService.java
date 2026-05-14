package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.PastReservationNotAllowedException;
import roomescape.reservation.exception.ReservationAccessDeniedException;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
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
    private final Logger logger = LoggerFactory.getLogger(ReservationTimeRepository.class);


    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation save(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(request.timeId()));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ThemeNotFoundException(request.themeId()));

        reservationRepository.findByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())
                .ifPresent(reservation -> {
                    throw new ReservationDuplicatedException(request.date(), request.timeId(), request.themeId());
                });

        if (isPassed(request.date(), reservationTime.getStartAt())) {
            throw new PastReservationNotAllowedException();
        }

        Reservation reservation = Reservation.of(
                request.name(),
                request.date(),
                reservationTime,
                theme);
        return reservationRepository.save(reservation);
    }

    private Boolean isPassed(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        logger.info("현재 시각 - %s".formatted(now));
        return localDateTime.isBefore(now);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByName(String name) {
        return reservationRepository.findByName(name);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void cancelByIdAndName(Long id, String name) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        validateOwner(reservation, name);
        reservationRepository.deleteById(id);
    }

    @Transactional
    public Reservation updateByIdAndName(Long id, String name, ReservationUpdateRequest request) {
        if (request.isEmpty()) {
            throw new IllegalArgumentException("변경할 예약 정보가 없습니다.");
        }
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        validateOwner(reservation, name);

        LocalDate date = getDateOrDefault(request, reservation);
        ReservationTime reservationTime = getReservationTimeOrDefault(request, reservation);
        Theme theme = getThemeOrDefault(request, reservation);

        if (isPassed(date, reservationTime.getStartAt())) {
            throw new PastReservationNotAllowedException();
        }

        Reservation updatedReservation = Reservation.of(id, reservation.getName(), date, reservationTime, theme);
        return reservationRepository.update(updatedReservation);
    }

    private void validateOwner(Reservation reservation, String name) {
        if (!reservation.getName().equals(name)) {
            throw new ReservationAccessDeniedException();
        }
    }

    private LocalDate getDateOrDefault(ReservationUpdateRequest request, Reservation reservation) {
        if (request.date() == null) {
            return reservation.getDate();
        }
        return request.date();
    }

    private ReservationTime getReservationTimeOrDefault(ReservationUpdateRequest request, Reservation reservation) {
        if (request.timeId() == null) {
            return reservation.getTime();
        }
        return reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(request.timeId()));
    }

    private Theme getThemeOrDefault(ReservationUpdateRequest request, Reservation reservation) {
        if (request.themeId() == null) {
            return reservation.getTheme();
        }
        return themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ThemeNotFoundException(request.themeId()));
    }
}
