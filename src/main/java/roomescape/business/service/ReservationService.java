package roomescape.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationDateTime;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.business.domain.theme.Theme;
import roomescape.presentation.dto.AdminReservationRequest;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;
import roomescape.presentation.exception.BadRequestException;
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

    public ReservationResponse createReservation(final ReservationRequest reservationRequest, final Member member) {
        final Reservation reservation = convertToReservation(reservationRequest, member);
        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation, member);
    }

    public ReservationResponse createReservation(final AdminReservationRequest reservationRequest) {
        final Member member = memberRepository.findById(reservationRequest.memberId())
                .orElseThrow(() -> new BadRequestException("예약자를 찾을 수 없습니다."));
        final Reservation reservation = convertToReservation(reservationRequest, member);
        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation, member);
    }

    public List<ReservationResponse> getReservations() {
        final List<Reservation> reservations = reservationRepository.findAll();
        final List<Member> members = memberRepository.findAll();
        return reservations.stream().flatMap(reservation ->
                members.stream()
                        .filter(member -> member.getName().equals(reservation.getName()))
                        .map(member -> new ReservationResponse(reservation, member))
        ).toList();
    }

    public void cancelReservationById(final long id) {
        reservationRepository.deleteById(id);
    }

    private Reservation convertToReservation(final AdminReservationRequest reservationRequest, final Member member) {
        return convertToReservation(member, reservationRequest.themeId(), reservationRequest.timeId(),
                reservationRequest.date());
    }

    private Reservation convertToReservation(final ReservationRequest reservationRequest, final Member member) {
        return convertToReservation(member, reservationRequest.themeId(), reservationRequest.timeId(),
                reservationRequest.date());
    }

    private Reservation convertToReservation(final Member member, final long l, final long l2, final LocalDate date) {
        final Theme theme = themeRepository.findById(l)
                .orElseThrow(() -> new BadRequestException("테마가 존재하지 않습니다."));
        final ReservationTime time = reservationTimeRepository.findById(l2)
                .orElseThrow(() -> new BadRequestException("예약 시간이 존재하지 않습니다."));
        final ReservationDateTime dateTime = new ReservationDateTime(date, time);
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("지나간 날짜와 시간은 예약 불가합니다.");
        }
        if (reservationRepository.existsByDateAndTimeAndTheme(dateTime.getDate(), dateTime.getTimeId(),
                theme.getId())) {
            throw new BadRequestException("해당 시간에 이미 예약이 존재합니다.");
        }
        return Reservation.register(member.getName(), date, time, theme);
    }
}
