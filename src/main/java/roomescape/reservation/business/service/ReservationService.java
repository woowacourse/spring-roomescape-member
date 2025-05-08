package roomescape.reservation.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.impl.BadRequestException;
import roomescape.global.exception.impl.ConflictException;
import roomescape.global.exception.impl.NotFoundException;
import roomescape.member.business.domain.Member;
import roomescape.member.business.repository.MemberDao;
import roomescape.reservation.business.domain.Reservation;
import roomescape.reservation.business.domain.ReservationTime;
import roomescape.reservation.business.repository.ReservationDao;
import roomescape.reservation.business.repository.ReservationTimeDao;
import roomescape.reservation.presentation.request.AdminReservationRequest;
import roomescape.reservation.presentation.request.MemberReservationRequest;
import roomescape.reservation.presentation.response.AvailableReservationTimeResponse;
import roomescape.reservation.presentation.response.ReservationResponse;
import roomescape.theme.business.domain.Theme;
import roomescape.theme.business.repository.ThemeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao,
                              final MemberDao memberDao) {
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

    public ReservationResponse addMemberReservation(MemberReservationRequest request, Long memberId) {
        return addReservation(request.timeId(), request.themeId(), memberId, request.date());
    }

    public ReservationResponse addAdminReservation(AdminReservationRequest request) {
        return addReservation(request.timeId(), request.themeId(), request.memberId(), request.date());
    }

    public void deleteById(Long id) {
        int affectedRows = reservationDao.deleteById(id);
        if (affectedRows == 0) {
            throw new NotFoundException("삭제할 예약정보가 없습니다.");
        }
    }

    public List<AvailableReservationTimeResponse> findAvailableReservationTime(Long themeId, String date) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        Theme selectedTheme = getTheme(themeId);
        List<Reservation> bookedReservations = reservationDao.findByDateAndThemeId(LocalDate.parse(date), themeId);
        return getAvailableReservationTimeResponses(reservationTimes, bookedReservations, selectedTheme);
    }

    public List<ReservationResponse> findReservationByThemeIdAndMemberIdInDuration(
            final long themeId,
            final long memberId,
            final LocalDate start,
            final LocalDate end
    ) {
        List<Reservation> reservations = reservationDao
                .findReservationByThemeIdAndMemberIdInDuration(themeId, memberId, start, end);
        return reservations.stream()
                .map(ReservationResponse::of)
                .toList();
    }

    private ReservationResponse addReservation(Long timeId, Long themeId, Long memberId, LocalDate date) {
        ReservationTime reservationTime = getReservationTime(timeId);
        Theme theme = getTheme(themeId);
        Member member = getMember(memberId);

        List<Reservation> sameTimeReservations = reservationDao.findByDateAndThemeId(date, themeId);

        validateIsBooked(sameTimeReservations, reservationTime, theme);
        validatePastDateTime(date, reservationTime.getStartAt());

        Reservation reservation = new Reservation(date, reservationTime, theme, member);
        Reservation saved = reservationDao.save(reservation);
        return ReservationResponse.of(saved);
    }

    private void validateIsBooked(List<Reservation> sameTimeReservations, ReservationTime reservationTime,
                                  Theme theme) {
        boolean isBooked = sameTimeReservations.stream()
                .anyMatch(reservation -> reservation.hasConflictWith(reservationTime, theme));
        if (isBooked) {
            throw new ConflictException("해당 테마 이용시간이 겹칩니다.");
        }
    }

    private void validatePastDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        if (reservationDateTime.isBefore(now)) {
            throw new BadRequestException("현재보다 과거의 날짜로 예약 할 수 없습니다.");
        }
    }

    private List<AvailableReservationTimeResponse> getAvailableReservationTimeResponses(
            List<ReservationTime> reservationTimes,
            List<Reservation> bookedReservations,
            Theme selectedTheme
    ) {
        List<AvailableReservationTimeResponse> responses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            boolean isBooked = bookedReservations.stream()
                    .anyMatch(reservation -> reservation.hasConflictWith(reservationTime, selectedTheme));
            AvailableReservationTimeResponse response = AvailableReservationTimeResponse
                    .from(reservationTime, isBooked);
            responses.add(response);
        }
        return responses;
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new NotFoundException("선택한 예약 시간이 존재하지 않습니다."));
    }

    private Theme getTheme(Long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new NotFoundException("선택한 테마가 존재하지 않습니다."));
    }

    private Member getMember(Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new NotFoundException("선택한 멤버가 존재하지 않습니다."));
    }
}
