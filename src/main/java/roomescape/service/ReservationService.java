package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.*;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ReservationTimeConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
            .map(this::createResponseDto)
            .toList();
    }

    public ReservationResponse add(ReservationRequest requestDto) {
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

        Reservation reservation = Reservation.create(requestDto.name(), requestDto.date(), reservationTime, theme);
        Reservation saved = reservationDao.save(reservation);
        return createResponseDto(saved);
    }

    public void deleteById(Long id) {
        reservationDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약정보가 없습니다."));

        reservationDao.deleteById(id);
    }

    private ReservationResponse createResponseDto(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(time.getId(), time.getStartAt());
        Theme theme = reservation.getTheme();
        ThemeResponse themeResponse = new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(), reservationTimeResponse, themeResponse);
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTime(Long themeId, String date) {
        // 1. 저장되어 있는 모슨 시간 조회
        // 2. date, themeId를 가진 예약들 조회
        // 3. 시간 조회결과를 순회하면서 response를 만드는데, 이때 2번에서 찾은 예약들을 보면서 isBooked 처리해주기

        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        Theme selectedTheme = themeDao.findById(themeId).orElseThrow(RuntimeException::new);
        List<Reservation> bookedReservations = reservationDao.findByDateAndThemeId(LocalDate.parse(date), themeId);
        List<AvailableReservationTimeResponse> responses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            boolean isBooked = bookedReservations.stream()
                .anyMatch(reservation -> reservation.isBooked(reservationTime, selectedTheme));
            AvailableReservationTimeResponse response = AvailableReservationTimeResponse.from(reservationTime, isBooked);
            responses.add(response);
        }
        return responses;
    }
}
