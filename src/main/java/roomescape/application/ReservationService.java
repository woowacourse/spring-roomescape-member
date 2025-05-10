package roomescape.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.param.CreateReservationParam;
import roomescape.application.param.ReservationSearchParam;
import roomescape.application.result.ReservationResult;
import roomescape.common.exception.BusinessRuleViolationException;
import roomescape.common.exception.NotFoundEntityException;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationTimeRepository reservationTImeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    public ReservationService(ReservationTimeRepository reservationTImeRepository,
                              ReservationRepository reservationRepository,
                              ThemeRepository themeRepository, MemberRepository memberRepository,
                              Clock clock) {
        this.reservationTImeRepository = reservationTImeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    public Long create(CreateReservationParam createReservationParam) {
        Member member = memberRepository.findById(createReservationParam.memberId())
                .orElseThrow(() -> new NotFoundEntityException(
                        createReservationParam.timeId() + "에 해당하는 member 튜플이 없습니다."));
        ReservationTime reservationTime = reservationTImeRepository.findById(createReservationParam.timeId())
                .orElseThrow(
                        () -> new NotFoundEntityException(
                                createReservationParam.timeId() + "에 해당하는 reservation_time 튜플이 없습니다."));
        Theme theme = themeRepository.findById(createReservationParam.themeId())
                .orElseThrow(() -> new NotFoundEntityException(
                        createReservationParam.themeId() + "에 해당하는 theme 튜플이 없습니다."));
        if (reservationRepository.existByDateAndTimeId(createReservationParam.date(), reservationTime.id())) {
            throw new BusinessRuleViolationException("날짜와 시간이 중복된 예약이 존재합니다.");
        }

        Reservation reservation = new Reservation(
                member,
                createReservationParam.date(),
                reservationTime,
                theme);
        reservation.validateReservable(LocalDateTime.now(clock));

        return reservationRepository.create(reservation);
    }

    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public List<ReservationResult> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResult::from)
                .toList();
    }

    public ReservationResult findById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundEntityException(reservationId + "에 해당하는 reservation 튜플이 없습니다."));
        return ReservationResult.from(reservation);
    }

    public List<ReservationResult> findReservationsBy(ReservationSearchParam reservationSearchParam) {
        List<Reservation> reservations = reservationRepository.findByThemeIdAndMemberIdBetweenDate(
                reservationSearchParam.themeId(),
                reservationSearchParam.memberId(), reservationSearchParam.from(), reservationSearchParam.to());
        return reservations.stream()
                .map(ReservationResult::from)
                .toList();
    }
}
