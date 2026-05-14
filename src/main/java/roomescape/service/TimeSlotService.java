package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.TimeSlot;
import roomescape.exception.ResourceInUseException;
import roomescape.exception.TimeSlotNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeSlotRepository;
import roomescape.service.dto.AvailableTimeSlot;

@Service
@Transactional(readOnly = true)
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
                .orElseThrow(() -> new TimeSlotNotFoundException(id));
    }

    @Transactional
    public TimeSlot saveTime(LocalTime startAt) {
        TimeSlot timeSlot = TimeSlot.transientOf(startAt);
        return timeSlotRepository.save(timeSlot);
    }

    @Transactional
    public void removeTime(long timeId) {
        try {
            timeSlotRepository.deleteById(timeId);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceInUseException("예약 시간");
        }
    }

    @Transactional
    public void putTime(long id, LocalTime startAt) {
        timeSlotRepository.update(new TimeSlot(id, startAt));
    }

    @Transactional
    public void patchTime(long id, LocalTime startAt) {
        TimeSlot timeSlot = findTimeSlotById(id);
        timeSlotRepository.update(timeSlot.patch(startAt));
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
