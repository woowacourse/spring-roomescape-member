package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao,
                              ReservationTimeDao reservationTimeDao,
                              ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAllReservations() {
        return reservationDao.getAllReservations();
    }

    //todo : 메소드로 묶기
    public Reservation addReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findReservationById(request.getTimeId());
        Theme theme = themeDao.findThemeById(request.getThemeId());

        LocalDateTime reservationDateTime = LocalDateTime.of(request.getDate(), reservationTime.getStartAt());

        LocalDateTime requestDateTime = reservationDateTime.truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (requestDateTime.isBefore(now)) {
            throw new BadRequestException("현재(%s) 이전 시간으로 예약할 수 없습니다.".formatted(now));
        }
        Long countReservation =
                reservationDao.countReservationByDateAndTimeId(request.getDate(), request.getTimeId());
        if (countReservation == null || countReservation > 0) {
            throw new DuplicatedException("이미 해당 시간(%s)에 예약이 존재합니다.".formatted(requestDateTime.toString()));
        }
        Reservation reservation = new Reservation(request.getName(), request.getDate(), reservationTime, theme);
        return reservationDao.addReservation(reservation);
    }

    public void deleteReservation(long id) {
        Long count = reservationDao.countReservationById(id);
        if (count == null || count <= 0) {
            throw new NotFoundException("해당 id:[%s] 값으로 예약된 내역이 존재하지 않습니다.".formatted(id));
        }
        reservationDao.deleteReservation(id);
    }

    public List<MemberReservationTimeResponse> getMemberReservationTimes(LocalDate date, long themeId) {
        List<ReservationTime> allTimes = reservationTimeDao.findAllReservationTimes();
        List<ReservationTime> bookedTimes = reservationDao.findReservationTimeByDateAndTheme(date, themeId);
        List<ReservationTime> notBookedTimes = filterNotBookedTimes(allTimes, bookedTimes);
        List<MemberReservationTimeResponse> bookedResponse = mapToResponse(bookedTimes, true);
        List<MemberReservationTimeResponse> notBookedResponse = mapToResponse(notBookedTimes, false);
        return concat(notBookedResponse, bookedResponse);
    }

    private List<ReservationTime> filterNotBookedTimes(List<ReservationTime> allTimes,
                                                       List<ReservationTime> bookedTimes) {
        return allTimes.stream()
                .filter(time -> !bookedTimes.contains(time))
                .toList();
    }

    private List<MemberReservationTimeResponse> mapToResponse(List<ReservationTime> times, boolean isBooked) {
        return times.stream()
                .map(time -> new MemberReservationTimeResponse(time.getId(), time.getStartAt(), isBooked))
                .toList();
    }

    private List<MemberReservationTimeResponse> concat(List<MemberReservationTimeResponse> notBookedTimes,
                                                       List<MemberReservationTimeResponse> bookedTimes) {
        return Stream.concat(notBookedTimes.stream(), bookedTimes.stream()).toList();
    }
}
