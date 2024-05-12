package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.admin.dto.AdminReservationSaveRequest;
import roomescape.global.auth.AuthUser;
import roomescape.global.exception.RoomEscapeException;
import roomescape.member.domain.ReservationMember;
import roomescape.member.service.MemberService;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dao.TimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationSaveRequest;
import roomescape.reservation.mapper.ReservationMapper;
import roomescape.theme.theme.dao.ThemeDao;
import roomescape.theme.theme.domain.Theme;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static roomescape.global.exception.ExceptionMessage.*;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper = new ReservationMapper();
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;
    private final MemberService memberService;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao, MemberService memberService) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
        this.memberService = memberService;
    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(reservationMapper::mapToResponse)
                .toList();
    }

    public List<ReservationResponse> findByMemberIdAndThemeIdAndDateBetween(Long memberId, Long themeId, LocalDate from, LocalDate to) {
        List<Reservation> reservations = reservationDao.findByMemberIdAndThemeIdAndDateBetween(memberId, themeId, from, to);
        return reservations.stream()
                .map(reservationMapper::mapToResponse)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationSaveRequest request, AuthUser authUser) {
        ReservationTime time = timeDao.findById(request.timeId())
                .orElseThrow(() -> new RoomEscapeException(NOT_FOUND, RESERVATION_TIME_NOT_FOUND.getMessage()));

        if (checkPastTime(request.date(), time)) {
            throw new RoomEscapeException(BAD_REQUEST, TIME_ALREADY_PAST.getMessage());
        }

        if (reservationDao.existByDateTimeTheme(request.date(), time.getStartAt(), request.themeId())) {
            throw new RoomEscapeException(BAD_REQUEST, RESERVATION_ALREADY_EXIST.getMessage());
        }
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(() -> new RoomEscapeException(NOT_FOUND, THEME_NOT_FOUND.getMessage()));

        ReservationMember member = new ReservationMember(authUser.id(), authUser.name());
        Reservation reservation = reservationMapper.mapToReservation(request, member, time, theme);
        Long saveId = reservationDao.save(reservation);
        return reservationMapper.mapToResponse(saveId, reservation);
    }

    public ReservationResponse saveReservation(AdminReservationSaveRequest request) {
        ReservationTime time = timeDao.findById(request.timeId())
                .orElseThrow(() -> new RoomEscapeException(NOT_FOUND, RESERVATION_TIME_NOT_FOUND.getMessage()));

        if (checkPastTime(request.date(), time)) {
            throw new RoomEscapeException(BAD_REQUEST, TIME_ALREADY_PAST.getMessage());
        }

        if (reservationDao.existByDateTimeTheme(request.date(), time.getStartAt(), request.themeId())) {
            throw new RoomEscapeException(BAD_REQUEST, RESERVATION_ALREADY_EXIST.getMessage());
        }
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(() -> new RoomEscapeException(NOT_FOUND, THEME_NOT_FOUND.getMessage()));

        ReservationMember member = memberService.findById(request.memberId());

        Reservation reservation = reservationMapper.mapToReservation(request, member, time, theme);
        Long saveId = reservationDao.save(reservation);
        return reservationMapper.mapToResponse(saveId, reservation);
    }

    private boolean checkPastTime(LocalDate date, ReservationTime time) {
        LocalDate now = LocalDate.now();
        return now.isAfter(date) || (now.isEqual(date) && time.inPast());
    }

    public void deleteReservationById(Long id) {
        reservationDao.deleteById(id);
    }
}
