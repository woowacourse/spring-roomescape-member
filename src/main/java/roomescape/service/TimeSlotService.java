package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeSlotRepository;

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

    public TimeSlot saveTime(LocalTime startAt) {
        TimeSlot timeSlot = TimeSlot.transientOf(startAt);
        return timeSlotRepository.save(timeSlot);
    }

    public void removeTime(long timeId) {
        timeSlotRepository.deleteById(timeId);
    }

    public TimeSlot findTime(long timeId) {
        return timeSlotRepository.findById(timeId);
    }

    public List<Long> findReserved(long themeId, LocalDate date) {
        return reservationRepository.findByThemeIdAndDate(themeId, date);
    }
}
