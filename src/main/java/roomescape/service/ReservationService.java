package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.common.exception.NotAbleReservationException;
import roomescape.common.exception.NotFoundReservationTimeException;
import roomescape.common.exception.NotFoundThemeException;
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
    // TODO: Repository 의존이 너무 많다. 개선할 방법이 없을까?
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
                .orElseThrow(() -> new NotFoundReservationTimeException("올바른 예약 시간을 찾을 수 없습니다. 나중에 다시 시도해주세요."));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NotFoundThemeException("올바른 방탈출 테마가 없습니다."));

        if (LocalDateTime.of(request.date(), reservationTime.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new NotAbleReservationException("과거 시점의 예약을 할 수 없습니다.");
        }

        List<ReservationTime> availableTimes = reservationTimeRepository.findAvailableTimesBy(request.date(),
                request.themeId());

        if (!availableTimes.contains(reservationTime)) {
            throw new NotAbleReservationException("이미 해당 시간과 테마에 예약이 존재하여 예약할 수 없습니다.");
        }

        Reservation createdReservation = reservationRepository.saveReservation(
                request.toReservation(reservationTime, theme));
        return ReservationResponse.of(createdReservation, ReservationTimeResponse.from(reservationTime),
                ThemeResponse.from(theme));
    }

    public List<ReservationResponse> readReservation() {
        List<Reservation> reservations = reservationRepository.readReservations();
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public void delete(Long id) {
        reservationRepository.deleteReservation(id);
    }
}
