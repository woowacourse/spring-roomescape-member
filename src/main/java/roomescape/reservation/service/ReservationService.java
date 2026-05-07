package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.service.ThemeService;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @Transactional
    public Reservation save(ReservationRequest request) {
        Reservation reservation = Reservation.create(
                request.name(),
                request.date(),
                reservationTimeService.getById(request.timeId()),
                themeService.getById(request.themeId())
        );

        checkDuplicateReservation(
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

    private void checkDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalStateException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
