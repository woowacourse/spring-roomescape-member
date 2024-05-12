package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationFactory;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationFactory reservationFactory;
    private final MemberRepository memberRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            ReservationFactory reservationFactory,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.reservationFactory = reservationFactory;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        ReservationTime requestedReservationTime = reservationTimeRepository.getById(reservationRequest.timeId());
        Theme requestedTheme = themeRepository.getById(reservationRequest.themeId());
        Member requestedMember = memberRepository.getById(reservationRequest.memberId());
        LocalDate requestedDate = LocalDate.parse(reservationRequest.date());

        Reservation requestedReservation = reservationFactory.createReservation(
                requestedMember,
                requestedDate,
                requestedReservationTime,
                requestedTheme);

        rejectDuplicateReservation(requestedReservation);

        Reservation savedReservation = reservationRepository.save(requestedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private void rejectDuplicateReservation(Reservation reservation) {
        boolean isDuplicateReservationPresent = reservationRepository.existsByThemeAndDateTime(
                reservation.getTheme(),
                reservation.getDate(),
                reservation.getTime());

        if (isDuplicateReservationPresent) {
            throw new IllegalStateException("중복된 예약이 존재합니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findByCondition(String name, Long themeId, String from, String to) {
        List<Reservation> reservations = reservationRepository.findByCondition(name, themeId, from, to);

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
