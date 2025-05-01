package roomescape.reservation.service;

import java.time.Clock;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.controller.request.ReservationCreateRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.domain.ReserverName;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.clock = clock;
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return ReservationResponse.from(reservations);
    }

    public ReservationResponse create(ReservationCreateRequest request) {
        Long timeId = request.timeId();
        ReservationDate reservationDate = new ReservationDate(request.date());

        if (reservationRepository.existSameDateTime(reservationDate, timeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 찼습니다.");
        }

        ReserverName reserverName = new ReserverName(request.name());
        ReservationTime reservationTime = reservationTimeService.getReservationTime(request.timeId());
        ReservationDateTime reservationDateTime = new ReservationDateTime(reservationDate, reservationTime, clock);
        Theme theme = themeService.getTheme(request.themeId());
        Reservation created = reservationRepository.save(reserverName, reservationDateTime, theme);

        return ReservationResponse.from(created);
    }

    public void deleteById(Long id) {
        Reservation reservation = getReservation(id);
        reservationRepository.deleteById(reservation.getId());
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 예약을 찾을 수 없습니다."));
    }
}
