package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Time;
import roomescape.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<Time> allTimes() {
        return timeRepository.findAll();
    }

    public Time saveTime(LocalTime startAt) {
        Time time = Time.transientOf(startAt);
        return timeRepository.save(time);
    }

    public void removeTime(long timeId) {
        timeRepository.deleteById(timeId);
    }

    public Time findTime(long timeId) {
        return timeRepository.findById(timeId);
    }
}
