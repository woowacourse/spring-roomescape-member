package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.controller.api.timeslot.dto.AddTimeSlotRequest;
import roomescape.controller.api.timeslot.dto.AvailabilityTimeSlotResponse;
import roomescape.controller.api.timeslot.dto.TimeSlotResponse;
import roomescape.exception.RoomescapeException;
import roomescape.model.Reservation;
import roomescape.model.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeSlotRepository;

@Service
public class TimeSlotService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(final ReservationRepository reservationRepository,
                           final TimeSlotRepository timeSlotRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlotResponse add(final AddTimeSlotRequest request) {
        final TimeSlot timeSlot = request.toEntity();
        final Long id = timeSlotRepository.save(timeSlot);
        final TimeSlot savedTimeSlot = new TimeSlot(id, timeSlot.startAt());
        return TimeSlotResponse.from(savedTimeSlot);
    }

    public List<TimeSlotResponse> findAll() {
        final List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        return TimeSlotResponse.from(timeSlots);
    }

    public boolean removeById(final Long id) {
        final List<Reservation> reservations = reservationRepository.findAllByTimeSlotId(id);
        if (!reservations.isEmpty()) {
            throw new RoomescapeException("삭제하려는 타임 슬롯을 사용하는 예약이 있습니다. 삭제하려는 타임 슬롯 ID: " + id);
        }
        return timeSlotRepository.removeById(id);
    }

    public List<AvailabilityTimeSlotResponse> findAvailableTimeSlots(final LocalDate date, final Long themeId) {
        final List<Reservation> filteredReservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        final List<TimeSlot> filteredTimeSlots = filteredReservations.stream()
                .map(Reservation::timeSlot)
                .toList();

        final List<TimeSlot> allTimeSlots = timeSlotRepository.findAll();
        return allTimeSlots.stream()
                .map(ts -> AvailabilityTimeSlotResponse.from(ts, filteredTimeSlots.contains(ts)))
                .toList();
    }
}
