package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.exception.AlreadyExistReservationException;
import roomescape.mapper.ReservationMapper;
import roomescape.repository.ReservationDao;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper = new ReservationMapper();
    private final ReservationTimeService reservationTimeService;
    private final ReservationDao reservationDao;
    //todo: service vs Dao 뭐가 좋을까?
    private final ThemeService themeService;

    public ReservationService(ReservationTimeService reservationTimeService, ReservationDao reservationDao, ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(reservationMapper::mapToResponse)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationSaveRequest request) {
        ReservationTime time = reservationTimeService.findTimeById(request.timeId());
        Theme theme = themeService.findThemeById(request.themeId());
        Reservation reservation = reservationMapper.mapToReservation(request, time, theme);

        if (reservationDao.existByDateTime(reservation.getDate(), time.getStartAt())) {
            throw new AlreadyExistReservationException("[ERROR] 같은 날짜, 시간에 중복된 예약을 생성할 수 없습니다.");
        }

        Long saveId = reservationDao.save(reservation);

        return reservationMapper.mapToResponse(saveId, reservation);
    }

    public void deleteReservationById(Long id) {
        reservationDao.deleteById(id);
    }
}
