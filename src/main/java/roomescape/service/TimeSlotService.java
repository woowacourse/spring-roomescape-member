package roomescape.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.model.Reservation;
import roomescape.model.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeSlotRepository;
import roomescape.service.dto.AvailableTimeSlotDto;

@Service
public class TimeSlotService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(ReservationRepository reservationRepository,
        TimeSlotRepository timeSlotRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot add(LocalTime startAt) {
        var timeSlot = TimeSlot.create(startAt);
        var id = timeSlotRepository.save(timeSlot);
        return TimeSlot.register(id, startAt);
    }

    public List<TimeSlot> allTimeSlots() {
        return timeSlotRepository.findAll();
    }

    public boolean removeById(long id) {
        List<Reservation> reservations = reservationRepository.findByTimeSlotId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalStateException("삭제하려는 타임 슬롯을 사용하는 예약이 있습니다.");
        }
        return timeSlotRepository.removeById(id);
    }

    public List<AvailableTimeSlotDto> availableTimeSlots(LocalDate date, long themeId) {
        var filteredReservations = reservationRepository.findByDateAndThemeId(date, themeId);
        var filteredTimeSlots = filteredReservations.stream()
            .map(Reservation::timeSlot)
            .toList();

        var allTimeSlots = timeSlotRepository.findAll();
        return allTimeSlots.stream()
            .map(ts -> AvailableTimeSlotDto.from(ts, filteredTimeSlots.contains(ts)))
            .toList();
    }
}
