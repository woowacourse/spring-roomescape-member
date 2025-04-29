package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.AddReservationDto;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationDao;
    private final ReservationTimeService reservationTimeService;

    public ReservationService(ReservationRepository reservationDao, ReservationTimeService reservationTimeService) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }

    @Transactional
    public long addReservation(AddReservationDto newReservation) {
        ReservationTime reservationTime = reservationTimeService.findReservationTimeById(newReservation.timeId());
        Reservation reservation = newReservation.toReservation(reservationTime);
        LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        validateAddReservationDateTime(reservation, currentDateTime);
        return reservationDao.add(reservation);
    }

    private void validateAddReservationDateTime(Reservation newReservation, LocalDateTime currentDateTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(newReservation.getDate(), newReservation.getStartAt());
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new InvalidReservationException("과거 시간에 예약할 수 없습니다.");
        }
    }

    public List<Reservation> allReservations() {
        return reservationDao.findAll();
    }
}
