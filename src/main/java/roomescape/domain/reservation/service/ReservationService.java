package roomescape.domain.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.domain.admin.dto.AdminReservationSaveRequest;
import roomescape.domain.member.domain.ReservationMember;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservation.dao.TimeDao;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.ReservationTime;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationSaveRequest;
import roomescape.domain.reservation.mapper.ReservationMapper;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.domain.Theme;
import roomescape.global.auth.AuthUser;
import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.List;

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

    public ReservationResponse saveReservation(ReservationSaveRequest request, AuthUser authUser) {
        ReservationTime time = timeDao.findById(request.timeId())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 예약 시간을 찾을 수 없습니다"));

        if (checkPastTime(request.date(), time)) {
            throw new RoomEscapeException("[ERROR] 이미 지난 시간입니다.");
        }

        if (reservationDao.existByDateTimeTheme(request.date(), time.getStartAt(), request.themeId())) {
            throw new RoomEscapeException("[ERROR] 같은 날짜, 테마, 시간에 중복된 예약을 생성할 수 없습니다.");
        }
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 테마를 찾을 수 없습니다"));

        ReservationMember member = new ReservationMember(authUser.id(), authUser.name());
        Reservation reservation = reservationMapper.mapToReservation(request, member, time, theme);
        Long saveId = reservationDao.save(reservation);
        return reservationMapper.mapToResponse(saveId, reservation);
    }

    public ReservationResponse saveReservation(AdminReservationSaveRequest request) {
        ReservationTime time = timeDao.findById(request.timeId())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 예약 시간을 찾을 수 없습니다"));

        if (checkPastTime(request.date(), time)) {
            throw new RoomEscapeException("[ERROR] 이미 지난 시간입니다.");
        }

        if (reservationDao.existByDateTimeTheme(request.date(), time.getStartAt(), request.themeId())) {
            throw new RoomEscapeException("[ERROR] 같은 날짜, 테마, 시간에 중복된 예약을 생성할 수 없습니다.");
        }
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 테마를 찾을 수 없습니다"));

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
