package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorType;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.reservation.controller.dto.MemberReservationRequest;
import roomescape.reservation.controller.dto.ReservationQueryRequest;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.MemberReservation;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.MemberReservationRepository;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final MemberReservationRepository memberReservationRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              MemberRepository memberRepository,
                              MemberReservationRepository memberReservationRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.memberReservationRepository = memberReservationRepository;
    }

    public List<ReservationResponse> findMemberReservations(ReservationQueryRequest request) {
        if (request.getThemeId() == null && request.getMemberId() == null) {
            return memberReservationRepository.findBy(request.getStartDate(), request.getEndDate()).stream()
                    .map(ReservationResponse::from)
                    .toList();
        }
        if (request.getThemeId() == null) {
            Member member = getMember(request.getMemberId());
            return memberReservationRepository.findBy(member, request.getStartDate(), request.getEndDate()).stream()
                    .map(ReservationResponse::from)
                    .toList();
        }
        if (request.getMemberId() == null) {
            Theme theme = getTheme(request.getThemeId());
            return memberReservationRepository.findBy(theme, request.getStartDate(), request.getEndDate()).stream()
                    .map(ReservationResponse::from)
                    .toList();
        }
        Member member = getMember(request.getMemberId());
        Theme theme = getTheme(request.getThemeId());
        return memberReservationRepository.findBy(member, theme, request.getStartDate(), request.getEndDate()).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse createMemberReservation(Member member, ReservationRequest reservationRequest) {
        ReservationTime reservationTime = getReservationTime(reservationRequest.timeId());
        Theme theme = getTheme(reservationRequest.themeId());

        LocalDate date = LocalDate.parse(reservationRequest.date());
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorType.INVALID_REQUEST_ERROR);
        }

        if (memberReservationRepository.existBy(date, reservationTime, theme)) {
            throw new BusinessException(ErrorType.DUPLICATED_RESERVATION_ERROR);
        }

        Reservation reservation = reservationRepository.save(new Reservation(date, reservationTime, theme));
        long memberReservationId = memberReservationRepository.save(member, reservation);

        return ReservationResponse.from(memberReservationId, reservation, member);
    }

    @Transactional
    public ReservationResponse createMemberReservation(MemberReservationRequest memberReservationRequest) {
        ReservationTime reservationTime = getReservationTime(memberReservationRequest.timeId());
        Theme theme = getTheme(memberReservationRequest.themeId());

        LocalDate date = LocalDate.parse(memberReservationRequest.date());

        if (reservationRepository.existsBy(date, reservationTime, theme)) {
            throw new BusinessException(ErrorType.DUPLICATED_RESERVATION_ERROR);
        }

        Member member = memberRepository.findById(memberReservationRequest.memberId()).orElseThrow();
        Reservation reservation = reservationRepository.save(new Reservation(date, reservationTime, theme));
        long memberReservationId = memberReservationRepository.save(member, reservation);
        return ReservationResponse.from(memberReservationId, reservation, member);
    }

    public void deleteMemberReservation(Member member, long memberReservationId) {
        MemberReservation memberReservation = getMemberReservation(memberReservationId);
        if (!memberReservation.isMember(member)) {
            throw new BusinessException(ErrorType.NOT_A_RESERVATION_MEMBER);
        }
        memberReservationRepository.deleteById(memberReservationId);
    }

    @Transactional
    public void delete(long reservationId) {
        memberReservationRepository.deleteByReservationId(reservationId);
        reservationRepository.delete(reservationId);
    }

    private ReservationTime getReservationTime(long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(ErrorType.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(ErrorType.THEME_NOT_FOUND));
    }

    private MemberReservation getMemberReservation(long memberReservationId) {
        return memberReservationRepository.findById(memberReservationId)
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_RESERVATION_NOT_FOUND));
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND));
    }
}
