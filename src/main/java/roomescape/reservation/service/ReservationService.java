package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.DuplicateResourceException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        validateDuplicateReservation(
                request.date(),
                request.timeId(),
                request.themeId()
        );

        ReservationTime time = reservationTimeService.getById(request.timeId());
        Theme theme = themeService.getById(request.themeId());

        Reservation reservation = Reservation.create(
                request.name(),
                request.date(),
                time,
                theme,
                LocalDateTime.now()
        );

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelById(Long id) {
        getById(id).validateCanCancel();
        reservationRepository.cancelById(id);
    }

    @Transactional
    public Reservation update(Long id, ReservationRequest request) {
        Reservation existing = getById(id);
        ReservationTime time = reservationTimeService.getById(request.timeId());
        Theme theme = themeService.getById(request.themeId());

        Reservation updated = existing.update(
                request.name(),
                request.date(),
                time,
                theme,
                LocalDateTime.now()
        );

        if (reservationRepository.existsByDateAndTimeIdAndThemeIdExcluding(
                request.date(), request.timeId(), request.themeId(), id)) {
            throw new DuplicateResourceException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }

        reservationRepository.update(updated);
        return updated;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll(LocalDateTime.now());
    }

    public List<Reservation> search(String name, LocalDate from, LocalDate to, Long themeId) {
        validatePeriodOrder(from, to);
        return reservationRepository.findByFilter(name, from, to, themeId, LocalDateTime.now());
    }

    private void validatePeriodOrder(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new BusinessRuleViolationException("from 은 to 보다 이전이어야 합니다.");
        }
    }

    private void validateDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new DuplicateResourceException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }

    @Transactional
    public int completeExpiredReservations() {
        return reservationRepository.completeAllPastReservations(LocalDateTime.now());
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 예약이 존재하지 않습니다. ID: " + id));
    }
}
