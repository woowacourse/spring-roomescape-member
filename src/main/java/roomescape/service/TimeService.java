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
import roomescape.dto.response.TimeResponseDto;

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

    public List<TimeResponseDto> findAll() {
        return timeDao.findAll().stream()
                .map(TimeResponseDto::from)
                .toList();
    }

    public TimeResponseDto findById(Long id) {
        return timeDao.findById(id)
                .map(TimeResponseDto::from)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));
    }

    @Transactional
    public TimeResponseDto create(TimeRequestDto timeRequest) {
        Time time = Time.create(timeRequest.startAt());

        if (timeDao.existsByStartAt(time.getStartAt())) {
            throw new ConflictException("이미 존재하는 시간 입니다.");
        }

        return TimeResponseDto.from(timeDao.create(TimeRow.from(time)));
    }

    @Transactional
    public void delete(Long id) {
        if(!timeDao.existsById(id)){
            throw new NotFoundException("존재하지 않는 시간입니다.");
        }

        if (reservationDao.existsByTimeId(id)) {
            throw new ConflictException("예약이 존재하여 시간을 삭제할 수 없습니다.");
        }
        timeDao.delete(id);
    }
}
