package roomescape.reservation.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.exception.ExistedException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.member.Member;
import roomescape.member.dao.MemberDao;
import roomescape.member.dto.response.MemberResponse;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.theme.Theme;
import roomescape.theme.dao.ThemeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservationDaoAll = reservationDao.findAll();
        return reservationDaoAll.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    public List<ReservationResponse> findAll(
            Long themeId,
            Long memberId,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        List<Reservation> reservationDaoAll = reservationDao.findAll(themeId, memberId, dateFrom, dateTo);
        return reservationDaoAll.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    private void validateDuplicate(@NotNull LocalDate date, LocalTime startAt) {
        if (reservationDao.findByDateTime(date, startAt).isPresent()) {
            throw new ExistedException("예약이 이미 존재합니다.");
        }
    }

    public void delete(Long id) {
        reservationDao.findById(id).orElseThrow(() -> new ReservationNotFoundException("예약이 존재하지 않습니다."));
        reservationDao.delete(id);
    }

    public ReservationResponse createReservation(@Valid ReservationCreateRequest request, Member member) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new ReservationNotFoundException("예약시간이 존재하지 않습니다."));
        validateDuplicate(request.date(), reservationTime.getStartAt());
        return reservationCreation(
                request.date(),
                request.timeId(),
                request.themeId(),
                member
        );
    }

    public ReservationResponse createReservation(@Valid ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new ReservationNotFoundException("예약시간이 존재하지 않습니다."));
        validateDuplicate(request.date(), reservationTime.getStartAt());

        final Member member = memberDao.findById(request.memberId())
                .orElseThrow(() -> new NoSuchElementException("예약자를 찾을 수 없습니다."));

        return reservationCreation(
                request.date(),
                request.timeId(),
                request.themeId(),
                member
        );
    }

    private ReservationResponse reservationCreation(LocalDate date, Long timeId, Long themeId, Member member) {
        final Theme theme = themeDao.findById(themeId)
                .orElseThrow(NoSuchElementException::new);

        final ReservationTime reservationTime = reservationTimeDao.findById(timeId)
                .orElseThrow(NoSuchElementException::new);

        final Reservation reservation = Reservation.createWithoutId(member, date, reservationTime, theme);
        Long id = reservationDao.create(reservation);

        return new ReservationResponse(
                id,
                new MemberResponse(member.getId(), member.getName()),
                date,
                new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt()),
                theme.getName()
        );
    }
}
