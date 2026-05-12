package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeSlotRepository;
import roomescape.service.dto.AvailableTimeSlot;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final ReservationRepository reservationRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, ReservationRepository reservationRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<TimeSlot> allTimes() {
        return timeSlotRepository.findAll();
    }

    public TimeSlot findTimeSlotById(long id) {
        return timeSlotRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 식별자로 데이터를 찾을 수 없습니다. id: " + id));
    }

    public TimeSlot saveTime(LocalTime startAt) {
        TimeSlot timeSlot = TimeSlot.transientOf(startAt);
        return timeSlotRepository.save(timeSlot);
    }

    public void removeTime(long timeId) {
        timeSlotRepository.deleteById(timeId);
    }

    public List<AvailableTimeSlot> findAvailableTimes(long themeId, LocalDate date) {
        List<TimeSlot> allTimeSlots = timeSlotRepository.findAll();
        List<Long> reservedIds = reservationRepository.findByThemeIdAndDate(themeId, date);
        return allTimeSlots.stream()
                .map(timeSlot -> mapToAvailable(timeSlot, reservedIds))
                .toList();
    }

    private AvailableTimeSlot mapToAvailable(TimeSlot timeSlot, List<Long> reservedIds) {
        boolean isAvailable = !reservedIds.contains(timeSlot.id());
        return new AvailableTimeSlot(timeSlot, isAvailable);
    }
}
