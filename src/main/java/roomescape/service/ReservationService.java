package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationDao.findAllReservation().stream()
            .map(ReservationResponseDto::from)
            .toList();
    }

    public ReservationResponseDto saveReservation(ReservationRequestDto reservationRequestDto) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        Person person = new Person(reservationRequestDto.name());
        ReservationDate date = new ReservationDate(LocalDate.parse(reservationRequestDto.date()));
        date.validateDate(currentDateTime.toLocalDate());
        ReservationTime reservationTime = reservationTimeDao.findById(
            reservationRequestDto.timeId());

        Reservation reservation = new Reservation(person, date, reservationTime);
        reservation.validateDateTime(date, reservationTime, currentDateTime);

        reservationDao.saveReservation(reservation);

        return ReservationResponseDto.from(reservation);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }
}
