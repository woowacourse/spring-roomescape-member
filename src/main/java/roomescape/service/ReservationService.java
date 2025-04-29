package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.ReservationCreation;

@Service
public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationService(final ReservationDAO reservationDAO, final ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationResponse addReservation(final ReservationCreation creation) {
        ReservationTime reservationTime = findReservationTimeByTimeId(creation.timeId());
        Reservation reservation = new Reservation(creation.name(), creation.date(), reservationTime);

        if (existsSameDateTime(reservation)) {
            throw new IllegalArgumentException("[ERROR] 같은 날짜/시간 예약이 존재합니다: date=%s, time=%s"
                    .formatted(reservation.getDate(), reservation.getTime().getStartAt()));
        }
        long savedId = reservationDAO.insert(reservation);
        Reservation savedReservation = reservationDAO.findById(savedId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR]"));

        return ReservationResponse.from(savedReservation);
    }

    private boolean existsSameDateTime(final Reservation reservation) {
        return reservationDAO.existsByDateAndTimeId(reservation.getDate(), reservation.getTime().getId());
    }

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }

    private ReservationTime findReservationTimeByTimeId(final long timeId) {
        return reservationTimeDAO.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 가능 시간입니다: timeId=%d"
                        .formatted(timeId)));
    }

    public boolean removeReservationById(final long id) {
        return reservationDAO.deleteById(id);
    }
}
