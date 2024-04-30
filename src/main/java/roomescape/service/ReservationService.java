package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.NotExistReservationException;
import roomescape.service.dto.ReservationInput;
import roomescape.service.dto.ReservationOutput;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationOutput createReservation(ReservationInput input) {
        //TODO : get 제거해야함
        ReservationTime time = reservationTimeDao.find(input.timeId()).get();
        Reservation reservation = input.toReservation(time);
        Reservation savedReservation = reservationDao.create(reservation);
        return ReservationOutput.toOutput(savedReservation);
    }

    public List<ReservationOutput> getAllReservations() {
        List<Reservation> reservations = reservationDao.getAll();
        return ReservationOutput.toOutputs(reservations);
    }

    public void deleteReservation(long id) {
        if (!reservationDao.isExistById(id)) {
            throw new NotExistReservationException(String.format(String.format("%d는 없는 id 입니다.", id)));
        }
        reservationDao.delete(id);
    }
}
