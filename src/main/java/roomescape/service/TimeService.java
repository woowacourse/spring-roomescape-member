package roomescape.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;
import roomescape.dto.TimeRequestDto;

import java.util.List;

@Service
@Transactional
public class TimeService {
    private final TimeDao timeDao;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    @Transactional(readOnly = true)
    public List<Time> findAll() {
        return timeDao.findAll();
    }


    @Transactional(readOnly = true)
    public Time findById(Long id) {
        return timeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
    }

    public Time create(TimeRequestDto timeRequest) {
        Time time = new Time(timeRequest.startAt());
        Long id = timeDao.insert(time);

        return timeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
    }

    public void delete(Long id) {
        Time time = timeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        timeDao.delete(time.getId());
    }
}
