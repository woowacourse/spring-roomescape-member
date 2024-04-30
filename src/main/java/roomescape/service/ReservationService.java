package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.mapper.ReservationMapper;
import roomescape.repository.ReservationDao;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper = new ReservationMapper();
    private final ReservationTimeService reservationTimeService;
    private final ReservationDao reservationDao;

    public ReservationService(ReservationTimeService reservationTimeService, ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(reservationMapper::mapToResponse)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationSaveRequest request) {
        ReservationTime time = reservationTimeService.findTimeById(request.timeId());
        Reservation reservation = reservationMapper.mapToReservation(request, time);
        Long saveId = reservationDao.save(reservation);

        return reservationMapper.mapToResponse(saveId, reservation);
    }

    public void deleteReservationById(Long id) {
        reservationDao.deleteById(id);
    }
}
