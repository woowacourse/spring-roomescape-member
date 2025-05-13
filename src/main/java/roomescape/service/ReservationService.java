package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.controller.member.dto.MemberInfoDto;
import roomescape.controller.reservation.dto.AdminReservationRequestDto;
import roomescape.controller.reservation.dto.MemberReservationRequestDto;
import roomescape.controller.reservation.dto.ReservationResponseDto;
import roomescape.controller.reservationTime.dto.ReservationTimeResponseDto;
import roomescape.controller.theme.dto.ThemeResponseDto;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

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
            MemberDao memberDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public ReservationResponseDto saveReservation(
            MemberReservationRequestDto memberReservationRequestDto,
            MemberInfoDto memberInfoDto
    ) {
        Reservation reservation = createReservation(memberReservationRequestDto, memberInfoDto);
        validateReservation(reservation);
        Long id = reservationDao.saveReservation(reservation);
        return buildReservationResponseDto(id, reservation);
    }

    public ReservationResponseDto saveReservation(AdminReservationRequestDto adminReservationRequestDto) {
        Reservation reservation = createReservation(adminReservationRequestDto);
        validateReservation(reservation);
        Long id = reservationDao.saveReservation(reservation);
        return buildReservationResponseDto(id, reservation);
    }


    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    public void cancelReservation(Long id) {
        reservationDao.deleteById(id);
    }


    public List<ReservationResponseDto> findReservationsByConditions(
            Long themeId,
            Long memberId,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        List<Reservation> reservations = reservationDao.findByConditions(themeId, memberId, dateFrom, dateTo);
        return reservations
                .stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    private Reservation createReservation(MemberReservationRequestDto dto, MemberInfoDto memberInfoDto) {
        ReservationTime time = getReservationTime(dto.timeId());
        Theme theme = getTheme(dto.themeId());
        Member member = getMember(memberInfoDto.id());

        return dto.convertToReservation(member, time, theme);
    }

    private Reservation createReservation(AdminReservationRequestDto dto) {
        ReservationTime time = getReservationTime(dto.timeId());
        Theme theme = getTheme(dto.themeId());
        Member member = getMember(dto.memberId());

        return dto.convertToReservation(time, theme, member);
    }

    private ReservationTime getReservationTime(Long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 예약 시각이 존재하지 않습니다."));
    }

    private Theme getTheme(Long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 테마가 존재하지 않습니다."));
    }

    private Member getMember(Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id 에 해당하는 멤버가 존재하지 않습니다."));
    }

    private void validateReservation(Reservation reservation) {
        reservation.validateReservationDateInFuture();

        reservationDao.findByDateAndTime(reservation)
                .ifPresent(r -> {
                    throw new DuplicatedException("이미 예약이 존재합니다.");
                });
    }

    private ReservationResponseDto buildReservationResponseDto(Long id, Reservation reservation) {
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();
        Member member = reservation.getMember();

        return new ReservationResponseDto(
                id,
                member.getName(),
                reservation.getDate(),
                new ReservationTimeResponseDto(time.getId(), time.getStartAt()),
                new ThemeResponseDto(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail())
        );
    }
}