package roomescape.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.TimeWithStatusResponse;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class ReservationFacade {

    private static final String ALREADY_EXISTS_DELETE_RESERVATION = "해당 시간과 테마를 사용 중인 예약이 존재하여 삭제할 수 없습니다.";
    private static final String ALREADY_EXISTS_ADD_RESERVATION = "해당 날짜와 시간, 테마에 이미 예약이 존재합니다.";

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationFacade(ReservationService reservationService, ReservationTimeService reservationTimeService,
                             ThemeService themeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @Transactional
    public void deleteTime(Long id) {
        if (reservationService.hasReservationsByTimeId(id)) {
            throw new IllegalArgumentException(ALREADY_EXISTS_DELETE_RESERVATION);
        }
        reservationTimeService.deleteTime(id);
    }

    @Transactional
    public void deleteTheme(Long id) {
        if (reservationService.hasReservationsByThemeId(id)) {
            throw new IllegalArgumentException(ALREADY_EXISTS_DELETE_RESERVATION);
        }
        themeService.deleteTheme(id);
    }

    @Transactional
    public Reservation addReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeService.findById(request.timeId());
        Theme theme = themeService.findById(request.themeId());

        if (reservationService.hasReservationsBy(request.date(), reservationTime.getId(),
                theme.getId())) {
            throw new IllegalArgumentException(ALREADY_EXISTS_ADD_RESERVATION);
        }
        return reservationService.addReservation(new Reservation(
                        request.name(),
                        request.date(),
                        reservationTime,
                        theme
                )
        );
    }

    public List<TimeWithStatusResponse> getTimesWithAvailability(LocalDate date, Long themeId) {
        List<ReservationTime> times = reservationTimeService.getReservationTimes();
        Set<Long> reservedTimeIds = reservationService.findReservedTimeIdsByDateAndThemeId(date, themeId);

        return times.stream()
                .map(time -> TimeWithStatusResponse.from(time, reservedTimeIds.contains(time.getId())))
                .toList();
    }
}
