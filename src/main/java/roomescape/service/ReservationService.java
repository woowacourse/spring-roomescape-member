package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.CreateReservationRequest;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAll();
    }

    public Reservation getReservation(Long id){
        return reservationDao.findById(id);
    }

    public Reservation createReservation(CreateReservationRequest request) {
        checkDuplicateReservation(request);

        Long newReservationId = reservationDao.save(request);
        return reservationDao.findById(newReservationId);
    }

    private void checkDuplicateReservation(CreateReservationRequest request) {
        boolean isDuplicate = reservationDao.isExistsByDateAndTimeIdAndThemeId(
                request.date(), request.timeId(), request.themeId());
        if (isDuplicate) {
            throw new IllegalStateException("해당 날짜·시간·테마에 이미 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
