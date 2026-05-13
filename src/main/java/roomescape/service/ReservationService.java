package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationDao;
import roomescape.dto.ReservationRequest;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }


    public ReservationResponse save(LocalDateTime now, ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        LocalDateTime time = LocalDateTime.of(request.date(), reservationTime.getStartAt());

        validateDateAndTimeNotPast(now,time);

        try{
            Long id = reservationDao.save(request.name(), request.date(), request.timeId(), request.themeId());
            Reservation reservation = reservationDao.findById(id);
            return ReservationResponse.from(reservation);
        } catch (DuplicateKeyException e){
            throw new IllegalArgumentException("중복 예약이 불가능합니다.");
        }

    }

    public List<ReservationResponse> findAllByName(String username) {
        return reservationDao.findByUserName(username).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }

    private void validateDateAndTimeNotPast(LocalDateTime now, LocalDateTime reservationTime) {
        if (now.isAfter(reservationTime)) {
            throw new IllegalArgumentException("과거 날짜/시간은 예약할 수 없습니다.");
        }
    }
}
