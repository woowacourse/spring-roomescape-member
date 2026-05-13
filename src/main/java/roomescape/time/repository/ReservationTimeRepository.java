package roomescape.time.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.domain.ReservationTime;
import roomescape.time.mapper.ReservationTimeMapper;
import roomescape.time.repository.dao.ReservationTimeDao;
import roomescape.time.repository.dto.CreateReservationTimeParams;
import roomescape.time.repository.dto.FindReservedTimeParams;
import roomescape.time.repository.entity.ReservationTimeEntity;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeRepository {

    private final ReservationTimeDao reservationTimeDao;

    @Transactional
    public ReservationTime save(CreateReservationTimeParams params) {
        Long id = reservationTimeDao.insert(params.startAt());
        return new ReservationTime(id, params.startAt());
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeMapper::toReservationTime)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        int deletedCount = reservationTimeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 ID입니다");
        }
    }

    public ReservationTime findById(Long id) {
         ReservationTimeEntity reservationTimeEntity = reservationTimeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

         return ReservationTimeMapper.toReservationTime(reservationTimeEntity);
    }

    public List<Long> findIdByCondition(FindReservedTimeParams params) {
        return reservationTimeDao.findReservedTimeIds(params.themeId(), params.date());
    }

    public boolean existsById(Long id) {
        return reservationTimeDao.existsById(id);
    }
}
