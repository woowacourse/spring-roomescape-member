package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.dto.response.ReservationTimeResponseDto;
import roomescape.dto.response.ThemeResponseDto;
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
        Reservation reservation = createReservation(reservationRequestDto);
        validateReservation(reservation);

        Long id = reservationDao.saveReservation(reservation);

        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();
        return new ReservationResponseDto(
                id,
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponseDto(time.getId(), time.getStartAt()),
                new ThemeResponseDto(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail())
        );
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public void cancelReservation(Long id) {
        reservationDao.deleteById(id);
    }

    private Reservation createReservation(ReservationRequestDto reservationRequestDto) {
        ReservationTime foundTime = reservationTimeDao.findById(reservationRequestDto.timeId())
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 예약 시각이 존재하지 않습니다."));

        Theme foundTheme = themeDao.findById(reservationRequestDto.themeId())
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 테마가 존재하지 않습니다."));

        return reservationRequestDto.convertToReservation(foundTime, foundTheme);
    }

    private void validateReservation(Reservation reservation) {
        reservation.validateReservationDateInFuture();

        reservationDao.findByDateAndTime(reservation)
                .ifPresent(foundReservation -> {
                    throw new DuplicatedException("이미 예약이 존재합니다.");
                });
    }
}
