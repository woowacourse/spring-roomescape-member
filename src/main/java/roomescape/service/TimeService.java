package roomescape.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.TimeRow;
import roomescape.domain.Time;
import roomescape.dto.request.TimeRequestDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<Time> findAll() {
        return timeDao.findAll().stream()
                .map(TimeRow::toDomain)
                .toList();
    }

    public Time findById(Long id) {
        return timeDao.findById(id)
                .map(TimeRow::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));
    }

    @Transactional
    public Time create(TimeRequestDto timeRequest) {
        if (timeDao.existsByStartAt(timeRequest.startAt())) {
            throw new ConflictException("이미 존재하는 시간 입니다.");
        }
        Time time = new Time(timeRequest.startAt());

        return timeDao.create(TimeRow.from(time)).toDomain();
    }

    @Transactional
    public void delete(Long id) {
        Time time = timeDao.findById(id)
                .map(TimeRow::toDomain)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));

        if (reservationDao.existsByTimeId(time.getId())) {
            throw new ConflictException("예약이 존재하여 시간을 삭제할 수 없습니다.");
        }
        timeDao.delete(time.getId());
    }
}
