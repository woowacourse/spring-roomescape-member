package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlots;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.AvailableTimeRequest;

@Service
public class ReservationSlotService {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public ReservationSlotService(ReservationService reservationService, ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    public ReservationSlots getReservationSlots(AvailableTimeRequest request) {
        List<ReservationTime> times = reservationTimeService.findAll();

        List<Reservation> alreadyReservedReservations = reservationService.findAllByDateAndThemeId(
                request.date(), request.themeId());

        return new ReservationSlots(times, alreadyReservedReservations);
    }
}
