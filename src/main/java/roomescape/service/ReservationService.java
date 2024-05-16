package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberRepository;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.MemberRequest;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse create(final AdminReservationRequest adminReservationRequest) {
        Theme theme = findThemeById(adminReservationRequest.themeId());
        ReservationTime reservationTime = findTimeById(adminReservationRequest.timeId());
        Member member = findLoginMemberById(adminReservationRequest.memberId());
        Reservation reservation = adminReservationRequest.toReservation(theme, reservationTime, member);
        Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    public ReservationResponse create(final ReservationRequest reservationRequest,
                                      MemberRequest memberRequest) {
        Theme theme = findThemeById(reservationRequest.themeId());
        ReservationTime reservationTime = findTimeById(reservationRequest.timeId());
        Reservation reservation = reservationRequest.toReservation(theme, reservationTime,
                memberRequest.toLoginMember(memberRequest));
        Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    private ReservationTime findTimeById(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 시간입니다."));
    }

    private Theme findThemeById(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 테마입니다."));
    }

    private Member findLoginMemberById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 사용자입니다."));
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findByThemeAndMemberAndDate(long themeId, long memberId, String dateFrom,
                                                                 String dateTo) {
        return reservationRepository.findByThemeAndMemberAndDate(themeId, memberId, dateFrom, dateTo)
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
