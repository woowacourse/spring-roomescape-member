package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Time;
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

    public List<Long> findReserved(long themeId, LocalDate date) {
        return reservationRepository.findByThemeIdAndDate(themeId, date);
    }
}
