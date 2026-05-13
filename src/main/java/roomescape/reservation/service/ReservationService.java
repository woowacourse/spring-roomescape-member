package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.CreateReservationParams;
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
        LocalDateTime reservationDateTime = LocalDateTime.of(date,
                reservationTimeRepository.findById(timeId).getStartAt());
        validateDateTimeIsNotPast(reservationDateTime);
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                new DuplicateReservationCondition(date, timeId, themeId))) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    private void validateDateTimeIsNotPast(LocalDateTime reservationDateTime) {
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 시간은 예약할 수 없습니다. 다른 날짜 및 시간을 선택해주세요.");
        }
    }

    private void validateThemeExists(Long id) {
        if (themeRepository.existsById(id)) {
            return;
        }
        throw new IllegalArgumentException("테마 정보가 유효하지 않습니다");
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        validateDateTimeIsNotPast(reservation.getReservationDateTime());

        reservationRepository.deleteById(id);
    }
}
