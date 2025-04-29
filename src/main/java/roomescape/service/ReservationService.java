package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse saveReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.readReservationTime(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("올바른 예약 시간을 찾을 수 없습니다. 나중에 다시 시도해주세요."));
        Theme theme = themeRepository.findById(request.themeId())
                // TODO: Custom Exception 만들기
                .orElseThrow(() -> new IllegalArgumentException("올바른 방탈출 테마가 없습니다."));
        Reservation createdReservation = reservationRepository.saveReservation(request.toReservation(reservationTime, theme));

        // TODO: themeId name 읽기
        return ReservationResponse.of(createdReservation, ReservationTimeResponse.from(reservationTime), ThemeResponse.from(theme));
    }

    public List<ReservationResponse> readReservation() {
        List<Reservation> reservations = reservationRepository.readReservations();
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public void delete(Long id) {
        reservationRepository.deleteReservation(id);
    }
}
