package roomescape.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.controller.reservation.dto.AddReservationRequest;
import roomescape.controller.reservation.dto.ReservationResponse;
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

    public ReservationResponse add(final AddReservationRequest request) {
        var member = findMember(request);
        var timeSlot = findTimeSlot(request);
        var theme = findTheme(request);
        var reservation = new Reservation(member, request.date(), timeSlot, theme);
        validateDuplicateReservation(reservation);

        var id = reservationRepository.save(reservation);
        var savedReservation = new Reservation(id, reservation.member(), reservation.date(), reservation.timeSlot(),
                reservation.theme());
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> findAll() {
        var reservations = reservationRepository.findAll();
        return ReservationResponse.from(reservations);
    }

    public boolean removeById(final Long id) {
        return reservationRepository.removeById(id);
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        var reservations = reservationRepository.findAll();
        var hasDuplicate = reservations.stream()
                .anyMatch(r -> r.isSameDateTime(reservation));
        if (hasDuplicate) {
            throw new RoomescapeException("이미 예약된 날짜와 시간에 대한 예약은 불가능합니다. 예약 날짜: " + reservation.date());
        }
    }

    private Member findMember(final AddReservationRequest request) {
        return memberRepository.findById(request.memberId())
                .orElseThrow(() -> new RoomescapeException("존재하지 않는 멤버입니다. 멤버 ID: " + request.memberId()));
    }

    private TimeSlot findTimeSlot(final AddReservationRequest request) {
        return timeSlotRepository.findById(request.timeId())
                .orElseThrow(() -> new RoomescapeException("존재하지 않는 타임 슬롯입니다. 타임 슬롯 ID: " + request.timeId()));
    }

    private Theme findTheme(final AddReservationRequest request) {
        return themeRepository.findById(request.themeId())
                .orElseThrow(() -> new RoomescapeException("존재하지 않는 테마입니다. 테마 ID: " + request.themeId()));
    }
}
