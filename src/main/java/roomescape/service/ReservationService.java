package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.CreateReservationRequest;
import roomescape.repository.ReservationDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAll();
    }

    public Reservation getReservation(Long id) {
        return reservationDao.findById(id);
    }

    public List<Reservation> getMyReservations(Long userId) {
        return reservationDao.findAllByUserId(userId);
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

    public void deleteMyReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationDao.findById(reservationId);
        if (!reservation.getUser().getId().equals(userId)) {
            throw new SecurityException("본인의 예약만 삭제할 수 있습니다.");
        }
        reservationDao.deleteById(reservationId);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
