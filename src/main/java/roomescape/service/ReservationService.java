package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.model.Reservation;
import roomescape.model.TimeSlot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeSlotRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public ReservationService(
        final ReservationRepository reservationRepository,
        final TimeSlotRepository timeSlotRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public Reservation reserve(String name, LocalDate date, Long timeId) {
        var timeSlot = findTimeSlot(timeId);
        var reservation = new Reservation(null, name, date, timeSlot);
        var id = reservationRepository.save(reservation);
        return new Reservation(id, name, date, timeSlot);
    }

    private TimeSlot findTimeSlot(final Long timeId) {
        return timeSlotRepository.findById(timeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public boolean removeById(long id) {
        return reservationRepository.removeById(id);
    }
}
