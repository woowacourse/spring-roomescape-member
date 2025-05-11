package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.AdminReservationCreationRequest;
import roomescape.dto.request.MemberReservationCreationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository,
                              final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public List<ReservationResponse> searchReservation(
            final Long themeId,
            final Long memberId,
            final LocalDate dateFrom,
            final LocalDate dateTo
    ) {
        List<Reservation> reservations = reservationRepository.saerch(themeId, memberId, dateFrom, dateTo);
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse getReservationById(long id) {
        Reservation reservation = loadReservationById(id);
        return ReservationResponse.from(reservation);
    }

    public long saveReservation(AdminReservationCreationRequest request) {
        Theme theme = loadThemeById(request.themeId());
        ReservationTime reservationTime = loadReservationTimeById(request.timeId());
        Member member = loadMemberById(request.memberId());
        Reservation reservation = Reservation.createWithoutId(
                member, request.date(), reservationTime, theme);

        reservation.validatePastDateTime();
        validateAlreadyReserved(reservation);

        return reservationRepository.add(reservation);
    }

    public long saveReservationForMember(MemberReservationCreationRequest request, Member member) {
        Theme theme = loadThemeById(request.themeId());
        ReservationTime reservationTime = loadReservationTimeById(request.timeId());
        Reservation reservation = Reservation.createWithoutId(
                member, request.date(), reservationTime, theme);

        reservation.validatePastDateTime();
        validateAlreadyReserved(reservation);

        return reservationRepository.add(reservation);
    }

    public void deleteReservation(long id) {
        validateReservationById(id);
        reservationRepository.deleteById(id);
    }

    private Reservation loadReservationById(long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        return reservation
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 예약이 존재하지 않습니다."));
    }

    private ReservationTime loadReservationTimeById(long reservationTimeId) {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(reservationTimeId);
        return reservationTime
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 예약시간이 존재하지 않습니다."));
    }

    private Theme loadThemeById(long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        return theme
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다."));
    }

    private Member loadMemberById(long memberId) {
        Optional<Member> member = memberRepository.findMemberById(memberId);
        return member.orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 회원이 존재하지 않습니다."));
    }

    private void validateAlreadyReserved(Reservation reservation) {
        boolean isAlreadyReserved = reservationRepository.checkAlreadyReserved(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId());
        if (isAlreadyReserved) {
            throw new BadRequestException("[ERROR] 이미 존재하는 예약입니다.");
        }
    }

    private void validateReservationById(long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isEmpty()) {
            throw new NotFoundException("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
        }
    }
}
