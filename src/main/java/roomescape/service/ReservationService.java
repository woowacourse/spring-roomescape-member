package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.model.Reservation;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponseDto saveReservation(ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationRequestDto.convertToReservation();
        Long id = reservationDao.saveReservation(reservation);
        return new ReservationResponseDto(
                id,
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponseDto(reservation.getTimeId(), reservation.getTime())
        );
    }

    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public void cancelReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
