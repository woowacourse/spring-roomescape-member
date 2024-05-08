package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.exception.ReservationBusinessException;
import roomescape.service.dto.ReservationConditionRequest;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationSaveRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository, MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse saveReservation(final ReservationSaveRequest reservationSaveRequest) {
        final ReservationTime time = reservationTimeRepository.findById(reservationSaveRequest.timeId())
                .orElseThrow(() -> new ReservationBusinessException("존재하지 않는 예약 시간입니다."));

        final Theme theme = themeRepository.findById(reservationSaveRequest.themeId())
                .orElseThrow(() -> new ReservationBusinessException("존재하지 않는 테마입니다."));

        final Member member = memberRepository.findById(reservationSaveRequest.memberId())
                .orElseThrow(() -> new ReservationBusinessException("존재하지 않는 회원입니다."));

        final Reservation reservation = new Reservation(member, reservationSaveRequest.date(), time, theme);
        validateUnique(reservation);

        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    private void validateUnique(final Reservation reservation) {
        final boolean isReservationExist = reservationRepository.existByDateAndTimeIdAndThemeId(reservation.getDate(),
                reservation.getTimeId(), reservation.getThemeId());

        if (isReservationExist) {
            throw new ReservationBusinessException("이미 존재하는 예약입니다.");
        }
    }

    public void deleteReservation(final Long id) {
        if (reservationRepository.findById(id).isEmpty()) {
            throw new ReservationBusinessException("존재하지 않는 예약입니다.");
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findReservationsByCondition(
            final ReservationConditionRequest reservationConditionRequest) {
        return reservationRepository.findByDurationAndThemeIdAndMemberId(
                        reservationConditionRequest.memberId(),
                        reservationConditionRequest.themeId(),
                        reservationConditionRequest.dateFrom(),
                        reservationConditionRequest.dateTo()
                ).stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
