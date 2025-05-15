package roomescape.service.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.member.MemberException;
import roomescape.common.exception.reservation.InvalidReservationException;
import roomescape.common.exception.reservationtime.InvalidReservationTimeException;
import roomescape.common.exception.theme.InvalidThemeException;
import roomescape.dao.member.MemberDao;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.reservationtime.ReservationTimeDao;
import roomescape.dao.theme.ThemeDao;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.admin.request.AdminReservationRequest;
import roomescape.dto.admin.request.SearchConditionRequest;
import roomescape.dto.reservation.request.AvailableReservationTimeRequestDto;
import roomescape.dto.reservation.request.ReservationRequestDto;
import roomescape.dto.reservation.response.AvailableReservationTimeResponseDto;
import roomescape.dto.reservation.response.ReservationResponseDto;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(final ReservationDao reservationDao,
                              final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao,
                              final MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public List<ReservationResponseDto> getAllReservations() {
        final List<Reservation> allReservation = reservationDao.findAllReservation();
        return allReservation.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public ReservationResponseDto saveReservation(final ReservationRequestDto request, final LoginMember loginMember) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        final ReservationDate reservationDate = getReservationDate((LocalDate.parse(request.date())), currentDateTime);
        final ReservationTime reservationTime = getReservationTime(request.timeId());
        final Theme theme = getTheme(request.themeId());
        final Member member = findMember(loginMember.id());

        final Reservation reservation = createReservation(member, reservationDate, reservationTime, theme);
        reservation.validateDateTime(reservationDate, reservationTime, currentDateTime);
        validateAlreadyReservation(reservationDate, request.timeId(), request.themeId());

        reservationDao.saveReservation(reservation);
        return ReservationResponseDto.from(reservation);
    }

    public ReservationResponseDto saveReservation(final AdminReservationRequest request) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        final ReservationDate reservationDate = getReservationDate(request.date(), currentDateTime);
        final ReservationTime reservationTime = getReservationTime(request.timeId());
        final Theme theme = getTheme(request.themeId());
        final Member member = findMember(request.memberId());

        final Reservation reservation = createReservation(member, reservationDate, reservationTime, theme);
        reservation.validateDateTime(reservationDate, reservationTime, currentDateTime);
        validateAlreadyReservation(reservationDate, request.timeId(), request.themeId());

        reservationDao.saveReservation(reservation);
        return ReservationResponseDto.from(reservation);
    }

    private ReservationDate getReservationDate(final LocalDate date, final LocalDateTime currentDateTime) {
        final ReservationDate reservationDate = new ReservationDate(date);
        reservationDate.validateDate(currentDateTime.toLocalDate());
        return reservationDate;
    }

    private ReservationTime getReservationTime(final Long id) {
        return reservationTimeDao.findById(id)
                .orElseThrow(() -> new InvalidReservationTimeException("해당 ID의 예약 시간을 찾을 수 없습니다."));
    }

    private Theme getTheme(final Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new InvalidThemeException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    private Reservation createReservation(final Member member, final ReservationDate date,
                                          final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(member, date, reservationTime, theme);
    }

    private Member findMember(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new MemberException("로그인하지 않은 회원입니다. 로그인 후에 예약을 할 수 있습니다."));
    }

    private void validateAlreadyReservation(final ReservationDate date, final Long timeId, final Long themeId) {
        if (reservationDao.existsReservationBy(date.date(), timeId, themeId)) {
            throw new InvalidReservationException("해당 날짜와 시간에 이미 같은 테마가 예약되어 있습니다.");
        }
    }

    public void deleteReservation(final Long id) {
        reservationDao.deleteReservation(id);
    }

    public List<AvailableReservationTimeResponseDto> getAvailableReservationTimes(
            final AvailableReservationTimeRequestDto request) {
        final List<ReservationTime> times = reservationTimeDao.findAllReservationTimes();
        return times.stream()
                .map(time -> createAvailableReservationTimeResponseDto(request, time))
                .toList();
    }

    private AvailableReservationTimeResponseDto createAvailableReservationTimeResponseDto(
            final AvailableReservationTimeRequestDto request,
            final ReservationTime time) {
        if (isAlreadyBookedTime(request.date(), time.getId(), request.themeId())) {
            return AvailableReservationTimeResponseDto.from(time, true);
        }
        return AvailableReservationTimeResponseDto.from(time, false);
    }

    private boolean isAlreadyBookedTime(final LocalDate date, final Long timeId, final Long themeId) {
        return reservationDao.existsReservationBy(date, timeId, themeId);
    }

    public List<ReservationResponseDto> findByCondition(final SearchConditionRequest request) {
        final Long themeId = request.themeId();
        final Long memberId = request.memberId();
        final LocalDate dateFrom = request.dateFrom();
        final LocalDate dateTo = request.dateTo();

        final List<Reservation> reservations = reservationDao.findByThemeIdAndMemberIDAndDateFromAndDateTo(
                dateFrom, dateTo, themeId, memberId);

        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }
}
