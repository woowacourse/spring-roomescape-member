package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequestDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationDate;
import roomescape.model.ReservationDateTime;
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

        ReservationDateTime reservationDateTime = new ReservationDateTime(
                ReservationDate.of(reservationRequestDto.date()), reservationTime);

        validateFutureDateTime(reservationDateTime);
        if (isAlreadyExist(reservationDateTime.getDate(), reservationRequestDto.timeId())){
            throw new IllegalArgumentException("Reservation already exists");
        }
        return reservationRepository.addReservation(reservationRequestDto, reservationTime);
    }

    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }

    private void validateFutureDateTime(ReservationDateTime reservationDateTime) {
        LocalDateTime dateTime = LocalDateTime.of(reservationDateTime.getDate().getDate(),reservationDateTime.getTime().getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 예약은 불가능합니다.");
        }
    }

    private boolean isAlreadyExist(ReservationDate reservationDate, Long timeId) {
         return reservationRepository.contains(reservationDate, timeId);
    }

}
