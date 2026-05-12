package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.CreateReservationRequest;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PastReservationException;
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

    public Reservation getReservation(Long id) {
        return reservationDao.findById(id);
    }

    public List<Reservation> getMyReservations(Long userId) {
        return reservationDao.findAllByUserId(userId);
    }

    public Reservation createReservation(CreateReservationRequest request) {
        checkDuplicateReservation(request);
        checkNotPast(request);

        Long newReservationId = reservationDao.save(request);
        return reservationDao.findById(newReservationId);
    }

    private void checkDuplicateReservation(CreateReservationRequest request) {
        boolean isDuplicate = reservationDao.isExistsByDateAndTimeIdAndThemeId(
                request.date(), request.timeId(), request.themeId());
        if (isDuplicate) {
            throw new DuplicateReservationException("선택하신 날짜·시간·테마에 이미 예약이 있습니다. 다른 시간을 선택해 주세요.");
        }
    }

    private void checkNotPast(CreateReservationRequest request) {
        ReservationTime time = reservationTimeDao.findById(request.timeId());
        LocalDateTime reservationAt = LocalDateTime.of(request.date(), time.getStartAt());
        if(reservationAt.isBefore(LocalDateTime.now())){
            throw new PastReservationException("지난 날짜는 예약할 수 없습니다. 오늘 이후 날짜를 선택해주세요.");
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
