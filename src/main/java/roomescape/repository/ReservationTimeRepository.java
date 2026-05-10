package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.global.exception.ReservationTimeNotFoundException;
import roomescape.repository.dao.ReservationTimeDao;

@Repository
@RequiredArgsConstructor
public class ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTime save(ReservationTime reservationTime) {
        return reservationTimeDao.insert(reservationTime);
    }

    public boolean existsByStartAt(LocalTime startAt) {
        return reservationTimeDao.existsByStartAt(startAt);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.selectAll();
    }

    public void deleteById(Long id) {
        int deletedCount = reservationTimeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new ReservationTimeNotFoundException("존재하지 않는 ID입니다");
        }
    }

    public ReservationTime findById(Long id) {
        return reservationTimeDao.selectById(id)
                .orElseThrow(ReservationTimeNotFoundException::new);
    }

    public List<Long> findIdByCondition(Long themeId, LocalDate date) {
        return reservationTimeDao.selectReservedTimeIds(themeId, date);
    }
}
