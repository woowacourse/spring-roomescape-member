package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.service.ThemeService;
import roomescape.time.service.ReservationTimeService;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
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
        Reservation reservation = Reservation.create(
                request.name(),
                request.date(),
                reservationTimeService.getById(request.timeId()),
                themeService.getById(request.themeId())
        );

        reservation.validateNotPast(LocalDateTime.now(clock));

        validateDuplicateReservation(
                reservation.getDate(),
                request.timeId(),
                request.themeId()
        );

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        reservationRepository.deleteAll();
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByFilter(LocalDate date, Long themeId) {
        return reservationRepository.findByFilter(date, themeId);
    }

    private void validateDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalStateException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
