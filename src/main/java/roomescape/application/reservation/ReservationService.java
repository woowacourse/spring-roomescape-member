package roomescape.application.reservation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.reservation.dto.request.ReservationRequest;
import roomescape.application.reservation.dto.response.ReservationResponse;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeRepository;
import roomescape.domain.reservation.Theme;
import roomescape.domain.reservation.ThemeRepository;


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
        Theme theme = themeRepository.getById(request.themeId());
        ReservationTime reservationTime = reservationTimeRepository.getById(request.timeId());

        if (reservationRepository.existsBy(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 존재하는 예약입니다.");
        }
        Reservation reservation = request.toReservation(reservationTime, theme);
        if (reservation.isBefore(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("현재 시간보다 과거로 예약할 수 없습니다.");
        }
        return ReservationResponse.from(reservationRepository.create(reservation));
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        Reservation reservation = reservationRepository.getById(id);
        reservationRepository.deleteById(reservation.getId());
    }
}
