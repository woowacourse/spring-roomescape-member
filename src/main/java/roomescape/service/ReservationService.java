package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationTimeConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            .map(ReservationResponse::of)
            .toList();
    }

    public ReservationResponse add(ReservationRequest requestDto) {
        ReservationTime reservationTime = getReservationTime(requestDto);
        Theme theme = getTheme(requestDto);
        List<Reservation> sameTimeReservations = reservationDao.findByDateAndThemeId(requestDto.date(), requestDto.themeId());

        validateIsBooked(sameTimeReservations, reservationTime, theme);
        validatePastDateTime(requestDto.date(), reservationTime.getStartAt());

        Reservation reservation = new Reservation(requestDto.name(), requestDto.date(), reservationTime, theme);
        Reservation saved = reservationDao.save(reservation);
        return ReservationResponse.of(saved);
    }

    public void deleteById(Long id) {
        reservationDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약정보가 없습니다."));

        reservationDao.deleteById(id);
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTime(Long themeId, LocalDate date) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        List<ReservationTime> bookedReservationTimes = reservationTimeDao.findBookedTimes(date, themeId);
        return getAvailableReservationTimeResponses(reservationTimes, bookedReservationTimes);
    }

    private ReservationTime getReservationTime(ReservationRequest requestDto) {
        return reservationTimeDao.findById(requestDto.timeId())
            .orElseThrow(() -> new EntityNotFoundException("선택한 예약 시간이 존재하지 않습니다."));
    }

    private Theme getTheme(ReservationRequest requestDto) {
        return themeDao.findById(requestDto.themeId())
            .orElseThrow(() -> new EntityNotFoundException("선택한 테마가 존재하지 않습니다."));
    }

    private void validateIsBooked(List<Reservation> sameTimeReservations, ReservationTime reservationTime, Theme theme) {
        boolean isBooked = sameTimeReservations.stream()
            .anyMatch(reservation -> reservation.isBooked(reservationTime, theme));
        if (isBooked) {
            throw new ReservationTimeConflictException("해당 테마 이용시간이 겹칩니다.");
        }
    }

    private void validatePastDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        if (reservationDateTime.isBefore(now)) {
            throw new PastReservationException("현재보다 과거의 날짜로 예약 할 수 없습니다.");
        }
    }

    private List<AvailableReservationTimeResponse> getAvailableReservationTimeResponses(List<ReservationTime> reservationTimes, List<ReservationTime> bookedReservationTimes) {
        List<AvailableReservationTimeResponse> responses = new ArrayList<>();

        for (ReservationTime reservationTime : reservationTimes) {
            boolean isBooked = bookedReservationTimes.contains(reservationTime);
            AvailableReservationTimeResponse response = AvailableReservationTimeResponse.from(reservationTime, isBooked);
            responses.add(response);
        }
        return responses;
    }
}
