package roomescape.application;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.ReservationRepository;
import roomescape.presentation.dto.request.ReservationCreateRequest;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationDateTime;
import roomescape.domain.ReserverName;
import roomescape.domain.Theme;
import roomescape.domain.ReservationTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final CurrentTimeService currentTimeService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeService reservationTimeService,
                              ThemeService themeService,
                              CurrentTimeService currentTimeService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.currentTimeService = currentTimeService;
    }

    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return ReservationResponse.from(reservations);
    }

    public ReservationResponse createReservation(ReservationCreateRequest request) {
        ReservationDate reservationDate = new ReservationDate(request.date());
        Long timeId = request.timeId();
        Long themeId = request.themeId();

        if (reservationRepository.existsByDateTimeAndTheme(reservationDate, timeId, themeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 찼습니다.");
        }

        ReserverName reserverName = new ReserverName(request.name());
        ReservationTime reservationTime = reservationTimeService.findReservationTimeById(timeId);
        ReservationDateTime reservationDateTime = ReservationDateTime.create(reservationDate, reservationTime, currentTimeService.now());
        Theme theme = themeService.findThemeById(themeId);
        Reservation created = reservationRepository.save(reserverName, reservationDateTime, theme);

        return ReservationResponse.from(created);
    }

    public void deleteReservationById(Long id) {
        Reservation reservation = findReservationById(id);
        reservationRepository.deleteById(reservation.getId());
    }

    private Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 예약을 찾을 수 없습니다."));
    }
}
