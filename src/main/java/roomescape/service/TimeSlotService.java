package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.model.TimeSlot;
import roomescape.repository.TimeSlotRepository;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot add(LocalTime startAt) {
        var timeSlot = new TimeSlot(null, startAt);
        var id = timeSlotRepository.save(timeSlot);
        return new TimeSlot(id, startAt);
    }

    public List<TimeSlot> allTimeSlots() {
        return timeSlotRepository.findAll();
    }

    public boolean removeById(long id) {
        return timeSlotRepository.removeById(id);
    }
}
