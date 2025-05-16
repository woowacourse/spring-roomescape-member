package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.Reservation;
import roomescape.service.dto.ReservationRecipe;
import roomescape.service.dto.ReservationResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    public static final int DELETE_FAILED_COUNT = 0;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ReservationThemeRepository reservationThemeRepository,
                              final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse addReservation(final ReservationRecipe recipe) {
        long timeId = recipe.timeId();
        final long themeId = recipe.themeId();
        final LocalDate date = recipe.date();
        validateUniqueReservation(date, timeId, themeId);
        final Member member = memberRepository.findById(recipe.memberId())
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 사용자 입니다."));
        final ReservationTime time = reservationTimeRepository.findById(timeId).orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 예약 시간 입니다."));
        final ReservationTheme theme = reservationThemeRepository.findById(themeId).orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 테마 입니다."));
        final Reservation reservation = new Reservation(member, date, time, theme);
        Reservation saved = reservationRepository.saveWithMember(reservation);
        return ReservationResponse.fromV2(saved);
    }

    public void removeReservation(final long id) {
        int deleteCounts = reservationRepository.deleteById(id);
        if (deleteCounts == DELETE_FAILED_COUNT) {
            throw new NoSuchElementException(String.format("[ERROR] 예약번호 %d번은 존재하지 않습니다.", id));
        }
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAllReservationsV2().stream()
                .map(ReservationResponse::fromV2)
                .toList();
    }


    public List<ReservationResponse> getFilteredReservations(final Long memberId, final Long themeId,
                                                       final LocalDate dateFrom, final LocalDate dateTo) {
        final List<Reservation> reservations = reservationRepository.findByMemberIdAndThemeIdAndDateFromAndDateTo(
                memberId, themeId, dateFrom, dateTo);
        return reservations.stream()
                .map(ReservationResponse::fromV2)
                .toList();
    }


    private void validateUniqueReservation(final LocalDate date, final long timeId, final long themeId) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 예약시간입니다.");
        }
    }
}
