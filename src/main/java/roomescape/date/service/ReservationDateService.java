package roomescape.date.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.ReservationDateRepository;
import roomescape.reservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationDateService {

    private final ReservationDateRepository reservationDateRepository;
    private final ReservationRepository reservationRepository;

    public ReservationDateService(ReservationDateRepository reservationDateRepository, ReservationRepository reservationRepository) {
        this.reservationDateRepository = reservationDateRepository;
        this.reservationRepository = reservationRepository;
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
    public ReservationDate updateStatus(Long dateId, boolean isActive) {
        ReservationDate reservationDate = getReservationDate(dateId);
        reservationDate.updateStatus(isActive);
        reservationDateRepository.updateStatus(reservationDate);
        return reservationDate;
    }

    @Transactional
    public ReservationDate deregister(Long id) {
        ReservationDate reservationDate = getReservationDate(id);
        validateAlreadyReserved(reservationDate.id());
        boolean isDeleted = reservationDateRepository.delete(reservationDate.id());
        validateIsDeleted(isDeleted);
        return reservationDate;
    }

    private ReservationDate getReservationDate(Long id) {
        return reservationDateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 예약날짜입니다."));
    }

    private void validateIsDeleted(boolean isDeleted) {
        if (!isDeleted) {
            throw new IllegalArgumentException("예약 시간을 삭제할 수 없습니다.");
        }
    }

    private void validateAlreadyReserved(Long dateId) {
        if (reservationRepository.existsByDateId(dateId)) {
            throw new IllegalArgumentException("해당 날짜에 예약이 존재하여, 삭제할 수 없습니다.");
        }
    }

}
