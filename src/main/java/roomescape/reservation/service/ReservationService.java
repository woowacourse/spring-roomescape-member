package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation save(ReservationRequest request) {
        String name = request.name();
        Long timeId = request.timeId();
        Long themeId = request.themeId();
        LocalDate date = request.date();
        Reservation reservation = createReservation(name, timeId, themeId, date);
        return reservationRepository.save(reservation);
    }

    private boolean isPastDateTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();
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

        Reservation updatedReservation = createReservation(name, reservationTime.getId(), theme.getId(), date);

        return reservationRepository.update(updatedReservation);
    }

    private void validateOwner(Reservation reservation, String name) {
        if (!reservation.getName().equals(name)) {
            throw new ReservationAccessDeniedException();
        }
    }

    private Reservation createReservation(String name, Long timeId, Long themeId, LocalDate date) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new ReservationTimeNotFoundException(timeId));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException(themeId));

        LocalDateTime localDateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        if (isPastDateTime(localDateTime)) {
            throw new PastReservationNotAllowedException();
        }

        reservationRepository.findByDateAndTimeIdAndThemeId(date, timeId, themeId)
                .ifPresent(reservation -> {
                    throw new ReservationDuplicatedException(date, timeId, themeId);
                });

        return Reservation.of(
                name,
                date,
                reservationTime,
                theme);
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
