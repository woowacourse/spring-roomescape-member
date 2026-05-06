package roomescape.reservation.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.exception.ReservationException;
import roomescape.reservation.repository.ReservationDetail;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    public List<ReservationResponse> findAllReservations() {
        List<ReservationDetail> result = reservationRepository.findAll();
        return result.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationCreateRequest request) {
        ThemeResponse themeResponse = ThemeResponse.from(themeService.findById(request.themeId()));
        ReservationTimeResponse timeResponse = ReservationTimeResponse.from(reservationTimeService.findById(request.timeId()));
        validateDuplicateReservation(request);
        Reservation reservation = request.toEntity(themeResponse.id(), timeResponse.id());

        return ReservationResponse.from(reservationRepository.save(reservation), themeResponse, timeResponse);
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    private void validateDuplicateReservation(ReservationCreateRequest request) {
        Boolean existsByDateAndTime = reservationRepository.existsByDateAndThemeAndTime(request.date(),
                request.themeId(),
                request.timeId()
        );
        if (existsByDateAndTime) {
            throw new ReservationException("[ERROR] 이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
