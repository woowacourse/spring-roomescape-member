package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.AvailableReservationTimeRequestDto;
import roomescape.dto.AvailableReservationTimeResponseDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.exception.InvalidThemeException;
import roomescape.exception.MemberException;

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
        List<Reservation> allReservation = reservationDao.findAllReservation();
        return allReservation.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public ReservationResponseDto saveReservation(final ReservationRequestDto request, final LoginMember loginMember) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        ReservationDate reservationDate = getReservationDate((LocalDate.parse(request.date())), currentDateTime);
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());
        Member member = findMember(loginMember.getId());

        Reservation reservation = createReservation(member, reservationDate, reservationTime, theme);
        reservation.validateDateTime(reservationDate, reservationTime, currentDateTime);
        validateAlreadyReservation(reservationDate, request.timeId(), request.themeId());

        reservationDao.saveReservation(reservation);
        return ReservationResponseDto.from(reservation);
    }

    public ReservationResponseDto saveReservation(final AdminReservationRequest request) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        ReservationDate reservationDate = getReservationDate(request.date(), currentDateTime);
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());
        Member member = findMember(request.memberId());

        Reservation reservation = createReservation(member, reservationDate, reservationTime, theme);
        reservation.validateDateTime(reservationDate, reservationTime, currentDateTime);
        validateAlreadyReservation(reservationDate, request.timeId(), request.themeId());

        reservationDao.saveReservation(reservation);
        return ReservationResponseDto.from(reservation);
    }

    private ReservationDate getReservationDate(final LocalDate date, final LocalDateTime currentDateTime) {
        ReservationDate reservationDate = new ReservationDate(date);
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

    private void validateAlreadyReservation(ReservationDate date, Long timeId, Long themeId) {
        if (reservationDao.existsReservationBy(date.getDate(), timeId, themeId)) {
            throw new InvalidReservationException("해당 날짜와 시간에 이미 같은 테마가 예약되어 있습니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }

    public List<AvailableReservationTimeResponseDto> getAvailableReservationTimes(
            AvailableReservationTimeRequestDto request) {
        List<ReservationTime> times = reservationTimeDao.findAllReservationTimes();
        return times.stream()
                .map(time -> createAvailableReservationTimeResponseDto(request, time))
                .toList();
    }

    private AvailableReservationTimeResponseDto createAvailableReservationTimeResponseDto(
            AvailableReservationTimeRequestDto request,
            ReservationTime time) {
        if (isAlreadyBookedTime(request.date(), time.getId(), request.themeId())) {
            return AvailableReservationTimeResponseDto.from(time, true);
        }
        return AvailableReservationTimeResponseDto.from(time, false);
    }

    private boolean isAlreadyBookedTime(LocalDate date, Long timeId, Long themeId) {
        return reservationDao.existsReservationBy(date, timeId, themeId);
    }
}
