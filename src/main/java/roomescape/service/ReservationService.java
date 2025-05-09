package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.entity.LoginMember;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.AuthenticationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationTimeConflictException;
import roomescape.repository.MemberDao;
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
    private final MemberDao memberDao;

    public ReservationService(
        ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao,
        ThemeDao themeDao,
        MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
            .map(ReservationResponse::of)
            .toList();
    }

    public ReservationResponse addByAdmin(AdminReservationRequest request) {
        LoginMember loginMember = memberDao.findById(request.memberId())
            .orElseThrow(() -> new AuthenticationException("로그인 정보가 존재하지 않습니다."));
        return createReservation(request.date(), request.timeId(), request.themeId(), loginMember);
    }

    public ReservationResponse add(ReservationRequest requestDto, LoginCheckRequest loginCheckRequest) {
        LoginMember loginMember = createLoginMember(loginCheckRequest);
        return createReservation(requestDto.date(), requestDto.timeId(), requestDto.themeId(), loginMember);
    }

    private ReservationResponse createReservation(LocalDate date, Long timeId, Long themeId, LoginMember loginMember) {
        ReservationTime reservationTime = getReservationTime(timeId);
        Theme theme = getTheme(themeId);
        List<Reservation> sameTimeReservations = reservationDao.findByDateAndThemeId(date, themeId);

        validateIsBooked(sameTimeReservations, reservationTime, theme);
        validatePastDateTime(date, reservationTime.getStartAt());

        Reservation reservation = new Reservation(loginMember, date, reservationTime, theme);
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

    public List<ReservationResponse> search(Long memberId, Long themeId, LocalDate from, LocalDate to) {
        List<Reservation> findReservations = reservationDao.findByMemberIdAndThemeIdAndDateBetween(memberId, themeId, from, to);
        return findReservations.stream()
            .map(ReservationResponse::of)
            .toList();
    }

    private LoginMember createLoginMember(LoginCheckRequest loginCheckRequest) {
        return new LoginMember(
            loginCheckRequest.id(), loginCheckRequest.name(), loginCheckRequest.email(), loginCheckRequest.role());
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeDao.findById(timeId)
            .orElseThrow(() -> new EntityNotFoundException("선택한 예약 시간이 존재하지 않습니다."));
    }

    private Theme getTheme(Long themeId) {
        return themeDao.findById(themeId)
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
