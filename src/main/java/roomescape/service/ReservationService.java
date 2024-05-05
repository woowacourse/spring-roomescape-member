package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.exception.AlreadyExistReservationException;
import roomescape.exception.IllegalThemeException;
import roomescape.exception.IllegalTimeException;
import roomescape.mapper.ReservationMapper;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper = new ReservationMapper();
    private final ReservationTimeService reservationTimeService;
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationTimeService reservationTimeService, ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeService = reservationTimeService;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(reservationMapper::mapToResponse)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationSaveRequest request) {
        ReservationTime time = reservationTimeService.findTimeById(request.timeId());
        if (LocalDate.now().isEqual(request.date()) && time.inPast()) {
            throw new IllegalTimeException("[ERROR] 이미 지난 시간입니다.");
        }

        if (request.themeId() == null) {
            throw new IllegalThemeException("[ERROR] 유효하지 않은 형식의 테마입니다.");
        }

        if (reservationDao.existByDateTimeTheme(request.date(), time.getStartAt(), request.themeId())) {
            throw new AlreadyExistReservationException("[ERROR] 같은 날짜, 테마, 시간에 중복된 예약을 생성할 수 없습니다.");
        }
        Theme theme = themeDao.findById(request.themeId());

        Reservation reservation = reservationMapper.mapToReservation(request, time, theme);

        Long saveId = reservationDao.save(reservation);

        return reservationMapper.mapToResponse(saveId, reservation);
    }

    public void deleteReservationById(Long id) {
        reservationDao.deleteById(id);
    }
}
