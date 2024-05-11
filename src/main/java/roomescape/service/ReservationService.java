package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ReservationFilterRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.dto.ReservationWithMemberSaveRequest;
import roomescape.model.*;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository, final ThemeRepository themeRepository, final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getReservations(final ReservationFilterRequest reservationFilterRequest) {
        validateReservationFilterDate(reservationFilterRequest);
        return reservationRepository.findByFilter(reservationFilterRequest)
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }

    private void validateReservationFilterDate(final ReservationFilterRequest request) {
        if (request.dateFrom() == null || request.dateTo() == null) {
            return;
        }
        if (request.dateFrom().isAfter(request.dateTo())) {
            throw new IllegalArgumentException(String.format("dateFrom이 dateTo 이후의 날짜입니다. {dateFrom: %s, dateTo: %s}", request.dateFrom(), request.dateTo()));
        }
    }

    public ReservationResponse saveReservation(final ReservationSaveRequest reservationSaveRequest, final LoginMember loginMember) {
        final ReservationTime time = getTimeById(reservationSaveRequest.timeId());
        final Theme theme = getThemeId(reservationSaveRequest.themeId());
        final Member member = new Member(loginMember.getId(), loginMember.getName(), loginMember.getRole(), loginMember.getEmail());
        final Reservation reservation = Reservation.createIfFuture(LocalDateTime.now(), member, reservationSaveRequest.date(), time, theme);
        validateUnique(reservation);

        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    private ReservationTime getTimeById(final Long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 예약 시간입니다. (%d)", reservationTimeId)));
    }

    private Theme getThemeId(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테마입니다. (%s)", themeId)));
    }

    private void validateUnique(final Reservation reservation) {
        final boolean isReservationExist = reservationRepository.existByDateAndTimeIdAndThemeId(reservation.getDate(),
                reservation.getTimeId(), reservation.getThemeId());
        if (isReservationExist) {
            throw new IllegalArgumentException("동일한 날짜, 시간, 테마에 대한 예약이 이미 존재합니다.");
        }
    }

    public ReservationResponse saveReservation(final ReservationWithMemberSaveRequest reservationSaveRequest) {
        final ReservationTime time = getTimeById(reservationSaveRequest.timeId());
        final Theme theme = getThemeId(reservationSaveRequest.themeId());
        final Member member = memberRepository.findById(reservationSaveRequest.memberId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 멤버입니다. (%d)", reservationSaveRequest.memberId())));
        final Reservation reservation = Reservation.createIfFuture(LocalDateTime.now(), member, reservationSaveRequest.date(), time, theme);
        validateUnique(reservation);

        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    public void deleteReservation(final Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 예약입니다. (%d)", id)));
        reservationRepository.deleteById(id);
    }
}
