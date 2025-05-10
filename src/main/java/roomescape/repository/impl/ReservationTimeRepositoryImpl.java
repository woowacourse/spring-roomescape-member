package roomescape.repository.impl;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.BookedReservationTimeResponseDto;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.ReservationTimeRepository;

@Component
public class ReservationTimeRepositoryImpl implements ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeRepositoryImpl(ReservationTimeDao reservationTimeDao,
        ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    @Override
    public void save(ReservationTime reservationTime) {
        long savedId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedId);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        if (reservationDao.existReservationByTime(id)) {
            throw new InvalidReservationException("이미 예약된 예약 시간을 삭제할 수 없습니다.");
        }
        reservationTimeDao.delete(id);
    }

    @Override
    public ReservationTime findById(Long id) {
        return reservationTimeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약시간을 찾을 수 없습니다"));
    }

    @Override
    public List<BookedReservationTimeResponseDto> findBooked(String date,
        Long themeId) {
        return reservationTimeDao.findBooked(date, themeId);
    }
}
