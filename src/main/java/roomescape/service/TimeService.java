package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<TimeSlot> allTimes() {
        return timeRepository.findAll();
    }

    public TimeSlot saveTime(LocalTime startAt) {
        TimeSlot timeSlot = TimeSlot.transientOf(startAt);
        return timeRepository.save(timeSlot);
    }

    public void removeTime(long timeId) {
        timeRepository.deleteById(timeId);
    }

    public TimeSlot findTime(long timeId) {
        return timeRepository.findById(timeId);
    }

    public List<Long> findReserved(long themeId, LocalDate date) {
        return reservationRepository.findByThemeIdAndDate(themeId, date);
    }
}
