package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.exception.DuplicateReservationException;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;
import roomescape.reservation.domain.exception.ReservationOwnerMismatchException;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
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
    public List<ReservationResponse> getReservations(String name) {
        List<Reservation> reservations = findReservations(name);
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private List<Reservation> findReservations(String name) {
        if (name == null || name.isBlank()) {
            return reservationRepository.findAll();
        }
        return reservationRepository.findByName(name);
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
        validateOwner(reservation, name);
        validateNotPast(reservation);
        reservationRepository.deleteById(id);
    }

    private void validateOwner(Reservation reservation, String name) {
        if (!reservation.getName().equals(name)) {
            throw new ReservationOwnerMismatchException("본인의 예약만 취소할 수 있습니다.");
        }
    }

    private void validateNotPast(Reservation reservation) {
        if (!reservationSchedulePolicy.canReserve(reservation.getDate(), reservation.getTime().getStartAt())) {
            throw new PastReservationException("이미 지난 예약은 취소할 수 없습니다.");
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
