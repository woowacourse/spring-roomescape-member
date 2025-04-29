package roomescape.reservation.service;

import jakarta.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservationDaoAll = reservationDao.findAll();
        return reservationDaoAll.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    public Long create(ReservationCreateRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId())
                .orElseThrow(NoSuchElementException::new);
        Reservation reservation = Reservation.createWithoutId(
                request.name(),
                request.date(),
                reservationTime
        );
        validateDuplicate(request.date(), reservationTime.getStartAt());
        return reservationDao.create(reservation);
    }

    private void validateDuplicate(@NotNull LocalDate date, LocalTime startAt) {
        if (reservationDao.findByDateTime(date, startAt).isPresent()) {
            throw new IllegalStateException("중복 예약은 불가능합니다.");
        }
    }

    public Integer delete(Long id) {
        reservationDao.findById(id).orElseThrow(() -> new NoSuchElementException("예약이 존재하지 않습니다."));
        return reservationDao.delete(id);
    }
}
