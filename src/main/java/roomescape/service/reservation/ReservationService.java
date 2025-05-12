package roomescape.service.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationSearchFilter;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.dto.member.UserReservationRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exceptions.reservation.ReservationDuplicateException;
import roomescape.infrastructure.member.MemberInfo;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservation.ReservationTimeRepository;
import roomescape.repository.reservation.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getReservations(ReservationSearchFilter reservationSearchFilter) {
        List<Reservation> reservations = reservationRepository.findAll();

        if (!reservationSearchFilter.isNeeded()) {
            return reservations.stream()
                    .map(ReservationResponse::from)
                    .toList();
        }

        return reservationSearchFilter.doFilter(reservations)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse addReservation(ReservationRequest request) {
        Member member = memberRepository.findById(request.memberId());
        ReservationTime time = reservationTimeRepository.findById(request.timeId());
        Theme theme = themeRepository.findById(request.themeId());
        Reservation reservation = Reservation.createNew(request.date(), time, theme, member);
        return ReservationResponse.from(saveReservation(reservation));

    }

    @Transactional
    public ReservationResponse addReservation(UserReservationRequest request, MemberInfo memberInfo) {
        Member member = memberRepository.findById(memberInfo.id());
        ReservationTime time = reservationTimeRepository.findById(request.timeId());
        Theme theme = themeRepository.findById(request.themeId());
        Reservation reservation = Reservation.createNew(request.date(), time, theme, member);
        return ReservationResponse.from(saveReservation(reservation));
    }

    @Transactional
    public void deleteReservation(long id) {
        reservationRepository.deleteById(id);
    }

    private Reservation saveReservation(Reservation reservation) {
        if (isDuplicate(reservation)) {
            throw new ReservationDuplicateException("해당 시각의 중복된 예약이 존재합니다.", reservation.getDate(),
                    reservation.getTime().getStartAt(), reservation.getTheme().getName());
        }
        return reservationRepository.save(reservation);
    }

    private boolean isDuplicate(Reservation reservation) {
        return reservationRepository.existsByDateAndTimeAndTheme(
                reservation.getDate(), reservation.getTime(), reservation.getTheme(), reservation.getMember());
    }
}
