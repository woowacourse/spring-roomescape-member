package roomescape.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.TimeSlot;
import roomescape.domain.dto.ReservationRequest;
import roomescape.domain.dto.ReservationResponse;
import roomescape.repository.ReservationDAO;
import roomescape.repository.TimeDAO;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final TimeDAO timeDAO;
    private final ReservationDAO reservationDAO;

    public ReservationService(TimeDAO timeDAO, ReservationDAO reservationDAO) {
        this.timeDAO = timeDAO;
        this.reservationDAO = reservationDAO;
    }

    public List<ReservationResponse> findEntireReservationList() {
        return reservationDAO.read()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(ReservationRequest reservationRequest) {
        TimeSlot timeSlot;
        timeSlot = getTimeSlot(reservationRequest);
        validate(reservationRequest.date(), timeSlot);
        Long reservationId = reservationDAO.create(reservationRequest);
        Reservation reservation = reservationRequest.toEntity(reservationId, timeSlot);
        return ReservationResponse.from(reservation);
    }

    private TimeSlot getTimeSlot(ReservationRequest reservationRequest) {
        TimeSlot timeSlot;
        try {
            timeSlot = timeDAO.findById(reservationRequest.timeId());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다");
        }
        return timeSlot;
    }

    private void validate(LocalDate date, TimeSlot timeSlot) {
        validateReservation(date, timeSlot);
        validateDuplicatedReservation(date, timeSlot.getId());
    }

    private void validateDuplicatedReservation(LocalDate date, Long timeId) {
        if (reservationDAO.isExists(date, timeId)) {
            throw new IllegalArgumentException("[ERROR] 예약이 찼어요 ㅜㅜ 죄송해요~~");
        }
    }
    
    public void delete(Long id) {
        reservationDAO.delete(id);
    }

    private void validateReservation(LocalDate date, TimeSlot time) {
        if (time == null || (time.isTimeBeforeNow() && !date.isAfter(LocalDate.now()))) {
            throw new IllegalArgumentException("[ERROR] 지나간 날짜와 시간으로 예약할 수 없습니다");
        }
    }
}
