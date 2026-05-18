package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReservationSchedulePolicy;
import roomescape.reservation.domain.exception.DuplicateReservationException;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;
import roomescape.reservation.domain.exception.ReservationOwnerMismatchException;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.reservation.presentation.dto.ReservationUpdateRequest;
import roomescape.reservation.presentation.dto.ReservationsResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.domain.exception.ReservationTimeNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationSchedulePolicy reservationSchedulePolicy;

    @Transactional(readOnly = true)
    public ReservationsResponse getReservations() {
        List<ReservationResponse> reservations = reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
        return ReservationsResponse.from(reservations);
    }

    @Transactional(readOnly = true)
    public ReservationsResponse getMyReservations(String name) {
        List<ReservationResponse> reservations = reservationRepository.findByName(name).stream()
                .map(ReservationResponse::from)
                .toList();
        return ReservationsResponse.from(reservations);
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        ReservationTime time = findReservationTime(request.timeId());
        Theme theme = findTheme(request.themeId());
        reservationSchedulePolicy.validateReservable(request.date(), time.getStartAt());
        validateNoDuplicate(request);
        return ReservationResponse.from(reservationRepository.save(ReservationRequest.toEntity(request, time, theme)));
    }

    public void cancelReservation(Long id, String name) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약ID 입니다."));
        validateOwner(reservation, name, "본인의 예약만 취소할 수 있습니다.");
        validateNotPast(reservation, "이미 지난 예약은 취소할 수 없습니다.");
        reservationRepository.deleteById(id);
    }

    public ReservationResponse updateReservation(Long id, ReservationUpdateRequest request) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약ID 입니다."));
        validateOwner(existing, request.name(), "본인의 예약만 변경할 수 있습니다.");
        validateNotPast(existing, "이미 지난 예약은 변경할 수 없습니다.");

        ReservationTime newTime = findReservationTime(request.timeId());
        reservationSchedulePolicy.validateReservable(request.date(), newTime.getStartAt());
        validateNoDuplicateForUpdate(existing, request);

        Reservation updated = Reservation.builder()
                .id(existing.getId())
                .name(existing.getName())
                .date(request.date())
                .time(newTime)
                .theme(existing.getTheme())
                .build();
        return ReservationResponse.from(reservationRepository.update(updated));
    }

    private void validateOwner(Reservation reservation, String name, String message) {
        if (!reservation.isOwnedBy(name)) {
            throw new ReservationOwnerMismatchException(message);
        }
    }

    private void validateNotPast(Reservation reservation, String message) {
        if (reservation.isPast(reservationSchedulePolicy)) {
            throw new PastReservationException(message);
        }
    }

    private void validateNoDuplicateForUpdate(Reservation existing, ReservationUpdateRequest request) {
        if (existing.isSameSlot(request.date(), request.timeId())) {
            return;
        }
        if (reservationRepository.existsByDateAndTimeAndTheme(
                request.date(), request.timeId(), existing.getThemeId())) {
            throw new DuplicateReservationException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    private void validateNoDuplicate(ReservationRequest request) {
        if (reservationRepository.existsByDateAndTimeAndTheme(
                request.date(), request.timeId(), request.themeId())) {
            throw new DuplicateReservationException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    private ReservationTime findReservationTime(Long id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new ReservationTimeNotFoundException("존재하지 않는 시간ID 입니다."));
    }

    private Theme findTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("존재하지 않는 테마입니다."));
    }
}
