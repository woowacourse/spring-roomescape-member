package roomescape.business.service.member;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.config.LoginMember;
import roomescape.exception.ReservationException;
import roomescape.exception.ReservationThemeException;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.member.dto.ReservationRequestDto;
import roomescape.presentation.member.dto.ReservationResponseDto;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationThemeRepository reservationThemeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponseDto::toResponse)
                .toList();
    }

    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationException("존재하지 않는 예약입니다."));
        return ReservationResponseDto.toResponse(reservation);
    }

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationDto, LoginMember loginMember) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationDto.timeId())
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        ReservationTheme theme = reservationThemeRepository.findById(reservationDto.themeId())
                .orElseThrow(() -> new ReservationThemeException("존재하지 않는 예약 테마입니다."));
        if (reservationTime.isInThePast(reservationDto.date())) {
            throw new ReservationException("과거 일시로 예약을 생성할 수 없습니다.");
        }
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new ReservationException("존재하지 않는 회원입니다."));
        Reservation reservation = new Reservation(member, reservationDto.date(), reservationTime, theme);
        validateDuplicatedReservation(reservation);
        return ReservationResponseDto.toResponse(reservationRepository.add(reservation));
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByReservation(reservation)) {
            throw new ReservationException("해당 날짜와 시간에 이미 예약이 존재합니다.");
        }
    }
}
