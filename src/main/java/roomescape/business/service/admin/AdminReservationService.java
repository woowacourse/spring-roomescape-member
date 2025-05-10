package roomescape.business.service.admin;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ReservationException;
import roomescape.exception.ReservationThemeException;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.admin.dto.AdminReservationRequestDto;
import roomescape.presentation.admin.dto.ReservationQueryCondition;
import roomescape.presentation.member.dto.ReservationResponseDto;

@Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final MemberRepository memberRepository;

    public AdminReservationService(ReservationRepository reservationRepository,
                                   ReservationTimeRepository reservationTimeRepository,
                                   ReservationThemeRepository reservationThemeRepository,
                                   MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ReservationResponseDto createReservation(AdminReservationRequestDto reservationRequestDto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequestDto.timeId())
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        ReservationTheme theme = reservationThemeRepository.findById(reservationRequestDto.themeId())
                .orElseThrow(() -> new ReservationThemeException("존재하지 않는 예약 테마입니다."));
        if (reservationTime.isInThePast(reservationRequestDto.date())) {
            throw new ReservationException("과거 일시로 예약을 생성할 수 없습니다.");
        }
        Member member = memberRepository.findById(reservationRequestDto.memberId())
                .orElseThrow(() -> new ReservationException("존재하지 않는 회원입니다."));
        Reservation reservation = new Reservation(member, reservationRequestDto.date(), reservationTime, theme);
        validateDuplicatedReservation(reservation);
        return ReservationResponseDto.toResponse(reservationRepository.add(reservation));
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByReservation(reservation)) {
            throw new ReservationException("해당 날짜와 시간에 이미 예약이 존재합니다.");
        }
    }

    public List<ReservationResponseDto> getAllReservationsByCondition(ReservationQueryCondition condition) {
        if (condition == null) {
            return reservationRepository.findAll()
                    .stream()
                    .map(ReservationResponseDto::toResponse)
                    .toList();
        }
        return reservationRepository.findAllByCondition(condition)
                .stream()
                .map(ReservationResponseDto::toResponse)
                .toList();
    }
}
