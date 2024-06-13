package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.reservation.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@Service
public class ReservationService {
    private static final int DELETE_SUCCESS = 1;
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberService memberService;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao, MemberService memberService) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberService = memberService;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();

    }

    public Reservation save(ReservationRequest request) {
        validateDuplicate(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId())
                .orElseThrow(()->new IllegalArgumentException(request.timeId()+"에 해당하는 시간은 존재하지 않습니다"));
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(()->new IllegalArgumentException(request.themeId()+"에 해당하는 테마는 존재하지 않습니다"));
        Member member = memberService.findMemberById(request.memberId());
        return reservationDao.save(new Reservation(member, request.date(), reservationTime, theme));
    }

    public Reservation validatePastAndSave(ReservationRequest request, Member member) {
        validateDuplicate(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId())
                .orElseThrow(()->new IllegalArgumentException(request.timeId()+"에 해당하는 시간은 존재하지 않습니다"));
        new ReservationDateTime(request, reservationTime).validatePast(LocalDateTime.now());
        Theme theme = themeDao.findById(request.themeId())
                .orElseThrow(()->new IllegalArgumentException(request.themeId()+"에 해당하는 테마는 존재하지 않습니다"));
        return reservationDao.save(new Reservation(member, request.date(), reservationTime, theme));

    }

    private void validateDuplicate(ReservationRequest request) {
        if (reservationDao.isDuplicate(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }
    }

    public void delete(long id) {
        if (reservationDao.deleteById(id) != DELETE_SUCCESS) {
            throw new IllegalArgumentException("Cannot delete a reservation by given id");
        }
    }

    public List<ReservationResponse> filter(long themeId, long memberId, String dateFrom, String dateTo) {
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);

        return reservationDao.filter(themeId, memberId, from, to)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
