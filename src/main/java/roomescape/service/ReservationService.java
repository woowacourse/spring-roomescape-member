package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.ThemeRepository;
import roomescape.service.request.LoginMember;
import roomescape.service.request.ReservationRequest;
import roomescape.service.response.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MemberRepository memberRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse createReservation(ReservationRequest request, LoginMember loginMember) {
        Member member = getMember(loginMember);
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());
        Reservation reservation = request.toDomain(member, reservationTime, theme);
        reservation.validateDateTime();
        validateDuplicateReservation(reservation);

        Reservation createdReservation = reservationRepository.create(reservation);
        return ReservationResponse.from(createdReservation);
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new NoSuchElementException("로그인 정보에 해당되는 회원이 없습니다."));
    }

    private ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당되는 예약 시간이 없습니다."));
    }

    private Theme getTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당되는 테마가 없습니다."));
    }

    private void validateDuplicateReservation(Reservation reservation) {
        if (reservationRepository.hasDuplicateReservation(reservation)) {
            throw new IllegalStateException("중복된 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.removeById(id)) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다. id를 확인하세요.");
        }
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
