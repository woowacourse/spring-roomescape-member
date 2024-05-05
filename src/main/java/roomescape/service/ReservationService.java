package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.reservation.ReservationRepository;

@Service
public class ReservationService {

    public static final int DAY_BEFORE_CALCULATE_DATE = 8;

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService, ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public ReservationResponse addReservation(ReservationAddRequest reservationAddRequest) {
        ReservationTimeResponse timeResponse = reservationTimeService.getTime(reservationAddRequest.timeId());
        ThemeResponse themeResponse = themeService.getTheme(reservationAddRequest.themeId());

        Reservation reservation = new Reservation(
                reservationAddRequest.name(),
                reservationAddRequest.date(),
                timeResponse.toReservationTime(),
                themeResponse.toTheme()
        );
        validateDateTime(reservation);
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    private void validateDateTime(Reservation reservation) {
        LocalDateTime localDateTime = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        LocalDateTime now = LocalDateTime.now();

        if (localDateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 시간은 예약할 수 없습니다.");
        }
        if (reservationRepository.hasSameReservation(reservation)) {
            throw new IllegalArgumentException("중복 예약을 할 수 없습니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    public List<ReservationResponse> findReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findPopularThemes(Long limitCount) {
        Map<Theme, Long> themeReservationCounts = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getDate().isAfter(LocalDate.now().minusDays(DAY_BEFORE_CALCULATE_DATE)))
                .filter(reservation -> reservation.getDate().isBefore(LocalDate.now()))
                .collect(Collectors.groupingBy(Reservation::getTheme, Collectors.counting()));

        return themeReservationCounts.entrySet().stream()
                .sorted(Map.Entry.<Theme, Long>comparingByValue().reversed())
                .limit(limitCount)
                .map(Map.Entry::getKey)
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }
}
