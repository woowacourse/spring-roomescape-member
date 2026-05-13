package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse reserve(CreateReservationRequest request) {
        validateReservationAvailable(request.date(), request.timeId(), request.themeId());
        CreateReservationParams params = new CreateReservationParams(request.name(), request.date(),
                request.timeId(), request.themeId());
        Reservation reservation = reservationRepository.save(params);

        return ReservationResponse.from(reservation);
    }

    private void validateReservationAvailable(LocalDate date, Long timeId, Long themeId) {
        validateThemeExists(themeId);
        validateReservationTimeExists(timeId);

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                new DuplicateReservationCondition(date, timeId, themeId))) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    private void validateReservationTimeExists(Long id) {
        if (reservationTimeRepository.existsById(id)) {
            return;
        }
        throw new IllegalArgumentException("예약 시간이 유효하지 않습니다");
    }

    private void validateThemeExists(Long id) {
        if (themeRepository.existsById(id)) {
            return;
        }
        throw new IllegalArgumentException("테마 정보가 유효하지 않습니다");
    }

    @Transactional
    public void cancelReservation(Long id) {
        int deletedCount = reservationRepository.deleteById(id);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("이미 삭제되었거나 존재하지 않는 예약입니다.");
        }
    }
}
