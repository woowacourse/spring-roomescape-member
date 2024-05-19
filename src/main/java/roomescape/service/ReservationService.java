package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.policy.CurrentDueTimePolicy;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.exception.member.AuthenticationFailureException;
import roomescape.exception.reservation.NotFoundReservationException;
import roomescape.exception.theme.NotFoundThemeException;
import roomescape.exception.time.NotFoundTimeException;
import roomescape.web.dto.request.reservation.ReservationRequest;
import roomescape.web.dto.request.reservation.ReservationSearchCond;
import roomescape.web.dto.response.reservation.ReservationResponse;

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

    public List<ReservationResponse> findAllReservation() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findAllReservationByConditions(ReservationSearchCond cond) {
        return reservationRepository.findByPeriodAndMemberAndTheme(cond.start(), cond.end(), cond.memberName(),
                        cond.themeName())
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationRequest request) {
        ReservationTime time = findReservationTimeById(request.timeId());
        Theme theme = findThemeById(request.themeId());
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(AuthenticationFailureException::new);

        Reservation verifiedReservation = verifyReservation(request, time, theme, member);
        Reservation savedReservation = reservationRepository.save(verifiedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private Reservation verifyReservation(ReservationRequest request, ReservationTime time, Theme theme,
                                          Member member) {
        Reservation reservation = request.toReservation(time, theme, member);
        List<Reservation> findReservations = reservationRepository.findByDateAndTimeIdAndThemeId(
                request.date(), request.timeId(), request.themeId());
        reservation.validateDateTimeReservation(new CurrentDueTimePolicy());
        reservation.validateDuplicateDateTime(findReservations);
        return reservation;
    }

    public void deleteReservation(Long id) {
        Reservation reservation = findReservationById(id);
        reservationRepository.delete(reservation);
    }

    private Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(NotFoundReservationException::new);
    }

    private ReservationTime findReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(NotFoundTimeException::new);
    }

    private Theme findThemeById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(NotFoundThemeException::new);
    }
}
