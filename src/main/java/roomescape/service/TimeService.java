package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.TimeSlot;
import roomescape.domain.dto.TimeSlotRequest;
import roomescape.domain.dto.TimeSlotResponse;
import roomescape.repository.ReservationDAO;
import roomescape.repository.TimeDAO;

import java.util.List;

@Service
public class TimeService {
    private final TimeDAO timeDAO;
    private final ReservationDAO reservationDAO;

    public TimeService(TimeDAO timeDAO, ReservationDAO reservationDAO) {
        this.timeDAO = timeDAO;
        this.reservationDAO = reservationDAO;
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
        validateDuplicatedTime(timeSlotRequest);
        Long id = timeDAO.create(timeSlotRequest);
        TimeSlot timeSlot = timeSlotRequest.toEntity(id);
        return TimeSlotResponse.from(timeSlot);
    }

    private void validateDuplicatedTime(TimeSlotRequest timeSlotRequest) {
        if (timeDAO.isExist(timeSlotRequest.startAt())) {
            throw new IllegalArgumentException("[ERROR] 이미 등록된 시간입니다");
        }
    }

    public void delete(Long id) {
        validateExistReservation(id);
        timeDAO.delete(id);
    }

    private void validateExistReservation(Long id) {
        if (reservationDAO.isExistsTimeId(id)) {
            throw new IllegalArgumentException("[ERROR] 예약이 등록된 시간은 제거할 수 없습니다");
        }
    }
}
