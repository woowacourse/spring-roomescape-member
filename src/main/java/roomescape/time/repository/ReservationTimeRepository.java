package roomescape.time.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.theme.mapper.ThemeMapper;
import roomescape.theme.repository.dao.ThemeDao;
import roomescape.theme.repository.entity.ThemeEntity;
import roomescape.time.domain.ReservationTime;
import roomescape.time.mapper.ReservationTimeMapper;
import roomescape.time.repository.dao.ReservationTimeDao;
import roomescape.time.repository.dto.CreateReservationTimeParams;
import roomescape.time.repository.entity.ReservationTimeEntity;

@Repository
public class ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationTimeRepository(ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationTime save(CreateReservationTimeParams params) {
        Long id = reservationTimeDao.insert(params.startAt());
        return new ReservationTime(id, params.startAt());
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.selectAll().stream()
                .map(ReservationTimeMapper::toReservationTime)
                .toList();
    }

    public void deleteById(Long id) {
        int deletedCount = reservationTimeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 ID입니다");
        }
    }

    public ReservationTime findById(Long id) {
         ReservationTimeEntity reservationTimeEntity = reservationTimeDao.selectById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

         return ReservationTimeMapper.toReservationTime(reservationTimeEntity);
    }
}
