package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        // TODO: 정적 팩토리 메서드로 임시 예약 생성하기
        var reservation = Reservation.create(name, date, timeSlot);
        // TODO : now 테스트 어려움 해결하기
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이전 날짜로 예약할 수 없습니다.");
        }
        var id = reservationRepository.save(reservation);
        return Reservation.register(id, name, date, timeSlot);
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
