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
            .map(ReservationResponse::of)
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
        return ReservationResponse.of(saved);
    }

    public void deleteById(Long id) {
        reservationDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약정보가 없습니다."));

        reservationDao.deleteById(id);
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTime(Long themeId, String date) {
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
