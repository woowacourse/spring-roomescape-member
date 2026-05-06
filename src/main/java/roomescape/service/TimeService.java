package roomescape.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;
import roomescape.dto.TimeRequestDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {
    private final TimeDao timeDao;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<Time> findAll() {
        return timeDao.findAll();
    }

    public Time findById(Long id) {
        return timeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
    }

    @Transactional
    public Time create(TimeRequestDto timeRequest) {
        Time time = new Time(timeRequest.startAt());
        return timeDao.insert(time);
    }

    @Transactional
    public void delete(Long id) {
        Time time = timeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        timeDao.delete(time.getId());
    }
}
