package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ReservationTimeConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.util.List;

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

    public List<ReservationResponseDto> findAll() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
            .map(this::createResponseDto)
            .toList();
    }

    public ReservationResponseDto add(ReservationRequestDto requestDto) {
        ReservationTime reservationTime = reservationTimeDao.findById(requestDto.timeId())
            .orElseThrow(() -> new EntityNotFoundException("선택한 예약 시간이 존재하지 않습니다."));

        Theme theme = themeDao.findById(requestDto.themeId())
            .orElseThrow(() -> new EntityNotFoundException("선택한 테마가 존재하지 않습니다."));

        if (reservationDao.isExist(requestDto.date(), requestDto.timeId())) {
            throw new DuplicateReservationException("이미 예약이 존재합니다.");
        }

        List<Reservation> sameTimeReservations = reservationDao.findByDateAndThemeId(requestDto.date(), requestDto.themeId());
        boolean isBooked = sameTimeReservations.stream()
            .anyMatch(reservation -> reservation.isBooked(reservationTime, theme));
        if (isBooked) {
            throw new ReservationTimeConflictException("해당 테마 이용시간이 겹칩니다.");
        }

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), reservationTime, theme);
        Reservation saved = reservationDao.save(reservation);
        return createResponseDto(saved);
    }

    public void deleteById(Long id) {
        reservationDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약정보가 없습니다."));

        reservationDao.deleteById(id);
    }

    private ReservationResponseDto createResponseDto(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        ReservationTimeResponseDto reservationTimeResponseDto = new ReservationTimeResponseDto(time.getId(), time.getStartAt());
        Theme theme = reservation.getTheme();
        ThemeResponseDto themeResponseDto = new ThemeResponseDto(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        return new ReservationResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(), reservationTimeResponseDto, themeResponseDto);
    }
}
