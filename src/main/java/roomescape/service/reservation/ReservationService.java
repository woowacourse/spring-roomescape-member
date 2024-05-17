package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.*;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.UnauthorizedException;
import roomescape.service.reservation.dto.AdminReservationRequest;
import roomescape.service.reservation.dto.ReservationFindRequest;
import roomescape.service.reservation.dto.ReservationRequest;
import roomescape.service.reservation.dto.ReservationResponse;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse create(AdminReservationRequest adminReservationRequest) {
        Member member = memberRepository.getById(adminReservationRequest.memberId());
        return createReservation(adminReservationRequest.timeId(), adminReservationRequest.themeId(), member, adminReservationRequest.date());
    }

    public ReservationResponse create(ReservationRequest reservationRequest, Member member) {
        return createReservation(reservationRequest.timeId(), reservationRequest.themeId(), member, reservationRequest.date());
    }

    private ReservationResponse createReservation(long timeId, long themeId, Member member, String date) {
        ReservationTime reservationTime = reservationTimeRepository.getById(timeId);
        Theme theme = themeRepository.getById(themeId);

        validate(date, reservationTime, theme);

        Reservation reservation = new Reservation(date, member, reservationTime, theme);

        return new ReservationResponse(reservationRepository.save(reservation));
    }

    private void validate(String date, ReservationTime reservationTime, Theme theme) {
        ReservationDate reservationDate = new ReservationDate(date);
        validateIfBefore(reservationDate, reservationTime);
        validateDuplicated(date, reservationTime, theme);
    }

    private void validateIfBefore(ReservationDate date, ReservationTime time) {
        Schedule schedule = new Schedule(date, time);
        if (schedule.isBeforeNow()) {
            throw new InvalidReservationException("현재보다 이전으로 일정을 설정할 수 없습니다.");
        }
    }

    private void validateDuplicated(String date, ReservationTime reservationTime, Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndTheme(date, reservationTime.getId(), theme.getId())) {
            throw new InvalidReservationException("선택하신 테마와 일정은 이미 예약이 존재합니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteById(long id) {
        reservationRepository.deleteById(id);
    }

    public void deleteById(long reservationId, Member member) {
        validateAuthority(reservationId, member.getId());
        reservationRepository.deleteById(reservationId);
    }

    private void validateAuthority(long reservationId, long memberId) {
        if (!reservationRepository.existsById(reservationId)) {
            return;
        }
        if (reservationRepository.getById(reservationId).isNotByMember(memberId)) {
            throw new UnauthorizedException("예약을 삭제할 권한이 없습니다.");
        }
    }

    public List<ReservationResponse> findByCondition(ReservationFindRequest reservationFindRequest) {
        return reservationRepository.findBy(reservationFindRequest.memberId(), reservationFindRequest.themeId(), reservationFindRequest.dateFrom(), reservationFindRequest.dateTo()).stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
