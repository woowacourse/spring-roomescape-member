package roomescape.service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequestDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationDate;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeService reservationTimeService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation addReservation(ReservationRequestDto reservationRequestDto) {
        ReservationTime reservationTime = reservationTimeService.getReservationTimeById(
                reservationRequestDto.timeId());
        validateDateTime(ReservationDate.of(reservationRequestDto.date()), reservationTime);
        return reservationRepository.addReservation(reservationRequestDto, reservationTime);
    }

    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }

    private void validateDateTime(ReservationDate reservationDate, ReservationTime reservationTime) {

        LocalDateTime dateTime = LocalDateTime.of(reservationDate.getDate(), reservationTime.getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new DateTimeException("과거 예약은 불가능합니다.");
        }
    }
}
