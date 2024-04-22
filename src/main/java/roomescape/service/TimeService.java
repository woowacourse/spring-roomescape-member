package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.TimeSlot;
import roomescape.domain.dto.TimeSlotRequest;
import roomescape.domain.dto.TimeSlotResponse;
import roomescape.repository.TimeDAO;

import java.util.List;

@Service
public class TimeService {
    private final TimeDAO timeDAO;

    public TimeService(TimeDAO timeDAO) {
        this.timeDAO = timeDAO;
    }

    public List<TimeSlotResponse> findAll() {
        return timeDAO.findAll()
                .stream()
                .map(TimeSlotResponse::from)
                .toList();
    }

    public TimeSlotResponse findById(Long id) {
        TimeSlot timeSlot = timeDAO.findById(id);
        return TimeSlotResponse.from(timeSlot);
    }

    public TimeSlotResponse create(TimeSlotRequest timeSlotRequest) {
        Long id = timeDAO.create(timeSlotRequest);
        TimeSlot timeSlot = timeSlotRequest.toEntity(id);
        return TimeSlotResponse.from(timeSlot);
    }

    public void delete(Long id) {
        timeDAO.delete(id);
    }
}
