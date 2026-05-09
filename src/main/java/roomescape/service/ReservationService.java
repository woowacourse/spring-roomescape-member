package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.web.dto.ReservationRequest;
import roomescape.web.dto.ReservationResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationResponse reserve(ReservationRequest request) {
        Theme theme = findThemeWithThrow(request.themeId());
        ReservationTime time = findTimeWithThrow(request.timeId());
        validateAlreadyReservation(request.date(), request.timeId(), time);

        Reservation reservation = Reservation.of(request.name(), request.date(), theme, time);

        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    @Transactional
    public void cancel(Long id) {
        reservationRepository.delete(id);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private void validateAlreadyReservation(LocalDate date, Long timeId, ReservationTime time) {
        if (reservationRepository.existByDateAndTimeId(date, timeId)) {
            throw new DuplicateEntityException("이미 예약 된 날짜입니다. (%s-%s)", date, time.getStartAt());
        }
    }

    private Theme findThemeWithThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 정보입니다."));
    }

    private ReservationTime findTimeWithThrow(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 시간 정보입니다."));
    }
}
