package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {
    public static final String INVALID_RESERVATION_ID = "요청한 예약을 찾을 수 없습니다.";
    private static final String DUPLICATED_RESERVATION = "이미 예약된 테마의 시간대입니다.";

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeService reservationTimeService,
                              ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation reserve(ReservationCreateRequest request) {
        ReservationTime reservationTime = reservationTimeService.find(request.getTimeId());
        Theme theme = themeService.find(request.getThemeId());

        if (reservationRepository.existsByTimeAndThemeAndDate(request.getTimeId(), request.getThemeId(),
                request.getDate())) {
            throw new IllegalArgumentException(DUPLICATED_RESERVATION);
        }

        Reservation reservation = Reservation.of(Name.from(request.getName()), ReservationDate.from(request.getDate()),
                reservationTime, theme);

        return reservationRepository.save(reservation);
    }

    public void cancel(long reservationId) {
        reservationRepository.findById(reservationId).orElseThrow(
                () -> new IllegalArgumentException(INVALID_RESERVATION_ID));

        reservationRepository.deleteById(reservationId);
    }
}
