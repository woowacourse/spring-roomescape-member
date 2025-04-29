package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponseDto saveReservation(ReservationRequestDto reservationRequestDto) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequestDto.timeId())
                .orElseThrow(() -> new IllegalStateException("id 에 해당하는 예약 시각이 존재하지 않습니다."));
        Theme theme = themeDao.findById(reservationRequestDto.themeId())
                .orElseThrow(() -> new IllegalStateException("id 에 해당하는 테마가 존재하지 않습니다."));

        Reservation reservation = reservationRequestDto.convertToReservation(reservationTime, theme);
        reservation.validateReservationDateInFuture();

        reservationDao.findByDateAndTime(reservation)
                .ifPresent(r -> {
                    throw new IllegalStateException("이미 예약이 존재합니다.");
                });

        Long id = reservationDao.saveReservation(reservation);
        return new ReservationResponseDto(
                id,
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponseDto(reservation.getTimeId(), reservation.getTime())
        );
    }

    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public void cancelReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
