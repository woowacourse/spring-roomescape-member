package roomescape.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마 입니다."));
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간 입니다."));

        LocalDateTime dateTime = LocalDateTime.of(request.date(), reservationTime.getStartAt());
        validateRequestDateAfterCurrentTime(dateTime);
        validateUniqueReservation(request);
        Reservation reservation = request.toReservation(reservationTime, theme);
        return ReservationResponse.from(reservationRepository.create(reservation));
    }

    private void validateRequestDateAfterCurrentTime(LocalDateTime dateTime) {
        LocalDateTime currentTime = LocalDateTime.now(clock);
        if (dateTime.isBefore(currentTime)) {
            throw new IllegalArgumentException("현재 시간보다 과거로 예약할 수 없습니다.");
        }
    }

    private void validateUniqueReservation(ReservationRequest request) {
        if (reservationRepository.existByDateAndTimeId(request.date(), request.timeId())) {
            throw new IllegalStateException("이미 존재하는 예약입니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return convertToReservationResponses(reservations);
    }

    private List<ReservationResponse> convertToReservationResponses(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 입니다."));
        reservationRepository.deleteById(reservation.getId());
    }
}
