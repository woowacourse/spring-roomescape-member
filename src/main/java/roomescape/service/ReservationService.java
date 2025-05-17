package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.controller.api.member.dto.LoginMemberInfo;
import roomescape.controller.api.reservation.dto.AddAdminReservationRequest;
import roomescape.controller.api.reservation.dto.AddReservationRequest;
import roomescape.controller.api.reservation.dto.ReservationResponse;
import roomescape.controller.api.reservation.dto.ReservationSearchFilter;
import roomescape.exception.RoomescapeException;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeSlotRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ReservationService(
            final ReservationRepository reservationRepository,
            final MemberRepository memberRepository,
            final TimeSlotRepository timeSlotRepository,
            final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse add(final AddAdminReservationRequest request) {
        final Member member = findMember(request.memberId());
        return createReservation(member, request.date(), request.timeId(), request.themeId());
    }

    public ReservationResponse add(final AddReservationRequest request, final LoginMemberInfo loginMemberInfo) {
        final Member member = findMember(loginMemberInfo.id());
        return createReservation(member, request.date(), request.timeId(), request.themeId());
    }

    private ReservationResponse createReservation(final Member member, final LocalDate date, final Long timeId,
                                                  final Long themeId) {
        final TimeSlot timeSlot = findTimeSlot(timeId);
        final Theme theme = findTheme(themeId);
        final Reservation reservation = new Reservation(member, date, timeSlot, theme);

        validateDuplicateReservation(reservation);

        final Long id = reservationRepository.save(reservation);
        final Reservation saved = new Reservation(id, member, date, timeSlot, theme);
        return ReservationResponse.from(saved);
    }

    public List<ReservationResponse> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return ReservationResponse.from(reservations);
    }

    public List<ReservationResponse> findAll(final ReservationSearchFilter searchFilter) {
        final List<Reservation> reservations = reservationRepository.findAllByFilter(searchFilter);
        return ReservationResponse.from(reservations);
    }

    public boolean removeById(final Long id) {
        return reservationRepository.removeById(id);
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        boolean hasDuplicate = reservationRepository.existsByDateTime(reservation.date(),
                reservation.timeSlot().startAt());
        if (hasDuplicate) {
            throw new RoomescapeException(
                    String.format("이미 예약된 슬롯에 대한 예약은 불가능합니다. 예약 날짜: %s, 시간: %s, 테마: %s", reservation.date(),
                            reservation.timeSlot(), reservation.theme()));
        }
    }

    private Member findMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException("존재하지 않는 멤버입니다. 멤버 ID: " + id));
    }

    private TimeSlot findTimeSlot(final Long id) {
        return timeSlotRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException("존재하지 않는 타임 슬롯입니다. 타임 슬롯 ID: " + id));
    }

    private Theme findTheme(final Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException("존재하지 않는 테마입니다. 테마 ID: " + id));
    }
}
