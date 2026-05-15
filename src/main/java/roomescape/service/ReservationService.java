package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationUpdateRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {
    private static final String INVALID_RESERVATION_ID = "요청한 예약을 찾을 수 없습니다.";
    private static final String DUPLICATED_RESERVATION = "해당 날짜, 테마, 시간에 이미 중복된 예약이 존재합니다.";
    private static final String PAST_RESERVATION = "지나간 날짜·시간에는 예약할 수 없습니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByName(String name) {
        return reservationRepository.findByName(name);
    }

    public Reservation reserve(ReservationCreateRequest request) {
        ReservationDate reservationDate = ReservationDate.from(request.getDate());
        ReservationTime reservationTime = reservationTimeService.find(request.getTimeId());
        Theme theme = themeService.find(request.getThemeId());

        validateFutureDateTime(reservationDate, reservationTime);
        validateNoDuplicate(request.getTimeId(), request.getThemeId(), reservationDate);

        Reservation reservation = Reservation.of(request.getName(), request.getDate(), reservationTime, theme);
        return reservationRepository.save(reservation);
    }

    public Reservation update(long reservationId, ReservationUpdateRequest request) {
        Reservation existing = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(INVALID_RESERVATION_ID));

        ReservationDate newDate = ReservationDate.from(request.getDate());
        ReservationTime newTime = reservationTimeService.find(request.getTimeId());

        validateFutureDateTime(newDate, newTime);
        validateNoDuplicateForUpdate(reservationId, request.getTimeId(), existing.getTheme().getId(), newDate);

        reservationRepository.update(reservationId, newDate.getDate(), request.getTimeId());

        return Reservation.of(reservationId, existing.getName(), newDate, newTime, existing.getTheme());
    }

    public void cancel(long reservationId) {
        reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(INVALID_RESERVATION_ID));
        reservationRepository.deleteById(reservationId);
    }

    private void validateFutureDateTime(ReservationDate reservationDate, ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(
                reservationDate.getDate(), reservationTime.getStartAt());
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessRuleViolationException(PAST_RESERVATION);
        }
    }

    private void validateNoDuplicate(Long timeId, Long themeId, ReservationDate reservationDate) {
        boolean isExists = reservationRepository.findByTimeAndTheme(timeId, themeId)
                .stream()
                .anyMatch(r -> r.getDate().equals(reservationDate));
        if (isExists) {
            throw new ConflictException(DUPLICATED_RESERVATION);
        }
    }

    private void validateNoDuplicateForUpdate(long excludeId, Long timeId, Long themeId,
            ReservationDate reservationDate) {
        boolean isExists = reservationRepository.findByTimeAndTheme(timeId, themeId)
                .stream()
                .filter(r -> r.getId() != excludeId)
                .anyMatch(r -> r.getDate().equals(reservationDate));
        if (isExists) {
            throw new ConflictException(DUPLICATED_RESERVATION);
        }
    }
}
