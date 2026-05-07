package roomescape.date.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.ReservationDateRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationDateService {

    private final ReservationDateRepository reservationDateRepository;

    public ReservationDateService(ReservationDateRepository reservationDateRepository) {
        this.reservationDateRepository = reservationDateRepository;
    }

    public ReservationDate readDate(Long id) {
        return getReservationDate(id);
    }

    public List<ReservationDate> readDates() {
        return reservationDateRepository.findAll();
    }

    public List<ReservationDate> readDatesAfterToday() {
        return reservationDateRepository.findAllAfterToday();
    }

    @Transactional
    public ReservationDate register(LocalDate date) {
        return reservationDateRepository.save(ReservationDate.create(date));
    }

    @Transactional
    public ReservationDate deregister(Long id) {
        ReservationDate reservationDate = getReservationDate(id);
        reservationDateRepository.delete(reservationDate.id());
        return reservationDate;
    }

    private ReservationDate getReservationDate(Long id) {
        return reservationDateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 예약날짜입니다."));
    }

}
