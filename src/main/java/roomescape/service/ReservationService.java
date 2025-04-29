package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Reservation reservation = reservationRequest.toEntityWithReservationTime(reservationTime);
        if (reservation.isPast(LocalDate.now())) {
            throw new IllegalArgumentException("하루 전 까지 예약 가능합니다.");
        }
        if (reservationDao.isExistByTimeIdAndDate(reservationRequest.timeId(), reservationRequest.date())) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    public void deleteReservation(Long id) {
        boolean isDeleted = reservationDao.deleteById(id);
        if (!isDeleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 id가 없습니다");
        }
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }
}
