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
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;

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

    public List<ReservationResponse> findMemberReservations() {
        return reservationRepository.findAllMemberReservation().stream().map(ReservationResponse::from).toList();
    }

    @Transactional
    public ReservationResponse createMemberReservation(Member member, ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findAndValidateReservationTime(reservationRequest.timeId());
        Theme theme = findAndValidateTheme(reservationRequest.themeId());

        LocalDate date = LocalDate.parse(reservationRequest.date());
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorType.INVALID_REQUEST_ERROR);
        }

        if (reservationRepository.existMemberReservationBy(date, reservationTime.getId(), theme.getId())) {
            throw new BusinessException(ErrorType.DUPLICATED_RESERVATION_ERROR);
        }

        Reservation reservation = reservationRepository.save(new Reservation(date, reservationTime, theme));
        long memberReservationId = reservationRepository.saveMemberReservation(member.getId(), reservation.getId());

        return ReservationResponse.from(memberReservationId, reservation, member);
    }

    public long create(MemberReservationRequest memberReservationRequest) {
        ReservationTime reservationTime = findAndValidateReservationTime(memberReservationRequest.timeId());
        Theme theme = findAndValidateTheme(memberReservationRequest.themeId());

        LocalDate date = LocalDate.parse(memberReservationRequest.date());

        if (reservationRepository.existsBy(date, reservationTime.getId(), theme.getId())) {
            throw new BusinessException(ErrorType.DUPLICATED_RESERVATION_ERROR);
        }

        Member member = memberRepository.findById(memberReservationRequest.memberId()).orElseThrow();
        Reservation reservation = reservationRepository.save(new Reservation(date, reservationTime, theme));
        reservationRepository.saveMemberReservation(member.getId(), reservation.getId());
        return reservation.getId();
    }

    public void deleteMemberReservation(long reservationMemberId) {
        if (!reservationRepository.deleteMemberReservationById(reservationMemberId)) {
            throw new BusinessException(ErrorType.MEMBER_RESERVATION_NOT_FOUND);
        }
    }

    @Transactional
    public void delete(long reservationId) {
        reservationRepository.deleteMemberReservationByReservationId(reservationId);
        boolean deleted = reservationRepository.delete(reservationId);
        if (!deleted) {
            throw new BusinessException(ErrorType.DUPLICATED_RESERVATION_ERROR);
        }
    }

    private ReservationTime findAndValidateReservationTime(long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new BusinessException(ErrorType.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme findAndValidateTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException(ErrorType.THEME_NOT_FOUND));
    }
}
