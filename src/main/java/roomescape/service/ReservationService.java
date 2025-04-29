package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;

@Component
public class ReservationService {

    private ReservationDao reservationDao;
    private TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<ReservationResponseDto> findAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequest) {
        ReservationTime reservationTime = timeDao.findById(reservationRequest.timeId());
        Reservation reservationWithoutId = reservationRequest.toReservationWith(reservationTime);
        long reservationId = reservationDao.create(reservationWithoutId);

        Reservation reservation = reservationWithoutId.copyWithId(new Id(reservationId));
        return ReservationResponseDto.from(reservation);
    }

    public void deleteReservation(long id) {
        reservationDao.deleteById(new Id(id));
    }
}
