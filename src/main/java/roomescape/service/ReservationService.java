package roomescape.service;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationFactory;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.infrastructure.persistence.MemberRepository;
import roomescape.infrastructure.persistence.ReservationRepository;
import roomescape.infrastructure.persistence.ReservationTimeRepository;
import roomescape.infrastructure.persistence.ThemeRepository;
import roomescape.infrastructure.persistence.dynamic.ReservationQueryConditions;
import roomescape.service.request.ReservationRequest;
import roomescape.service.response.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final ReservationFactory reservationFactory;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository,
            ReservationFactory reservationFactory
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.reservationFactory = reservationFactory;
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        validateDuplication(request);

        Reservation reservation = toDomain(request);
        Reservation createdReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(createdReservation);
    }

    private void validateDuplication(ReservationRequest request) {
        ReservationQueryConditions duplicatedConditions = ReservationQueryConditions.builder()
                .timeId(request.timeId())
                .themeId(request.themeId())
                .date(request.date())
                .build();

        if (reservationRepository.hasBy(duplicatedConditions)) {
            throw new IllegalStateException("중복된 예약이 존재합니다.");
        }
    }

    private Reservation toDomain(ReservationRequest request) {
        return reservationFactory.create(
                request.date(),
                getMember(request.memberId()),
                getReservationTime(request.timeId()),
                getTheme(request.themeId())
        );
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당되는 사용자가 없습니다."));
    }

    private ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당되는 예약 시간이 없습니다."));
    }

    private Theme getTheme(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당되는 테마가 없습니다."));
    }

    public void deleteReservation(Long id) {
        reservationRepository.removeById(id);
    }
}
