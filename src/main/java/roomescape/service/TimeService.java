package roomescape.service;


import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;
import roomescape.dto.request.TimeRequestDto;

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
        if (timeDao.existsByStartAt(timeRequest.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 시간 입니다.");
        }
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
