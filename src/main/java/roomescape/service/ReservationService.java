package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.LoginMemberDto;
import roomescape.dto.request.AdminReservationRequestDto;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.exception.BadRequestException;
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
    public Reservation createByAdmin(final AdminReservationRequestDto request) {
        final Member member = memberRepository.findById(request.getMemberId());
        final ReservationRequestDto reservationRequest = new ReservationRequestDto(
                request.getDate(),
                request.getTimeId(),
                request.getThemeId()
        );
        return createReservation(reservationRequest, member);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllWithConditions(
            final Long memberId,
            final Long themeId,
            final LocalDate dateFrom,
            final LocalDate dateTo
    ) {
        return reservationRepository.findAllWithConditions(memberId, themeId, dateFrom, dateTo);
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
