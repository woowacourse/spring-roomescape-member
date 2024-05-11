package roomescape.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.core.domain.Member;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;
import roomescape.core.domain.Theme;
import roomescape.core.dto.AdminReservationRequestDto;
import roomescape.core.dto.LoginMemberDto;
import roomescape.core.dto.ReservationRequestDto;
import roomescape.core.repository.MemberRepository;
import roomescape.core.repository.ReservationRepository;
import roomescape.core.repository.ReservationTimeRepository;
import roomescape.core.repository.ThemeRepository;
import roomescape.web.exception.AuthorizationException;
import roomescape.web.exception.BadRequestException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository,
            final MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Reservation create(final ReservationRequestDto request, final LoginMemberDto loginMember) {
        final Member member = loginMember.toMember();
        return createReservation(request, member);
    }

    @Transactional
    public Reservation createByAdmin(final AdminReservationRequestDto request, final LoginMemberDto loginMember) {
        if (loginMember == null || loginMember.isNotAdmin()) {
            throw new AuthorizationException("관리자가 아닙니다.");
        }
        final Member member = memberRepository.findById(request.getMemberId());
        final ReservationRequestDto reservationRequest = new ReservationRequestDto(
                request.getDate(),
                request.getTimeId(),
                request.getThemeId()
        );
        return createReservation(reservationRequest, member);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void delete(final long id) {
        reservationRepository.deleteById(id);
    }

    private Reservation createReservation(final ReservationRequestDto request, final Member member) {
        final ReservationTime time = reservationTimeRepository.findById(request.getTimeId());
        final Theme theme = themeRepository.findById(request.getThemeId());
        final Reservation reservation = new Reservation(member, request.getDate(), time, theme);

        validateDateTimeIsNotPast(reservation, time);
        validateDuplicatedReservation(reservation, time);
        final Long id = reservationRepository.save(reservation);
        return new Reservation(id, member, reservation.getDate(), time, theme);
    }

    private void validateDateTimeIsNotPast(final Reservation reservation, final ReservationTime reservationTime) {
        if (reservation.isDatePast()) {
            throw new BadRequestException("지난 날짜에는 예약할 수 없습니다.");
        }
        if (reservation.isDateToday() && reservationTime.isPast()) {
            throw new BadRequestException("지난 시간에는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(final Reservation reservation, final ReservationTime reservationTime) {
        final String date = reservation.getDateString();
        final Long timeId = reservationTime.getId();
        final Long themeId = reservation.getThemeId();

        if (reservationRepository.hasDuplicateReservation(date, timeId, themeId)) {
            throw new BadRequestException("예약 내역이 존재합니다.");
        }
    }
}
