package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.member.model.Member;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

@Service
public class ReservationService {

    private final JdbcReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final JdbcThemeRepository themeRepository;
    private final JdbcMemberRepository memberRepository;

    public ReservationService(
            final JdbcReservationRepository reservationRepository,
            final JdbcReservationTimeRepository reservationTimeRepository,
            final JdbcThemeRepository themeRepository,
            final JdbcMemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public CreateReservationResponse createReservation(
            final Long memberId,
            final LocalDate date,
            final Long themeId,
            final Long timeId
    ) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약 시간이 존재하지 않습니다."));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 테마가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원이 존재하지 않습니다."));

        validateReservationDateTime(date, reservationTime.getTime());
        Reservation reservation = new Reservation(null, member, date, reservationTime, theme);

        if (reservationRepository.existsByDateAndTimeAndTheme(
                reservation.getDate(), reservationTime.getId(), theme.getId())) {
            throw new IllegalStateException("동일한 시간의 예약이 존재합니다.");
        }

        return CreateReservationResponse.of(reservationRepository.save(reservation));
    }

    private void validateReservationDateTime(final LocalDate reservationDate, final LocalTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime);
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    public List<FindReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(FindReservationResponse::of)
                .toList();
    }

    public List<FindReservationResponse> getReservations(
            final Long memberId, final Long themeId,
            final LocalDate dateFrom, final LocalDate dateTo
    ) {
        return reservationRepository.findAllFilterOf(memberId, themeId, dateFrom, dateTo).stream()
                .map(FindReservationResponse::of)
                .toList();
    }

    public FindReservationResponse getReservation(final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
        return FindReservationResponse.of(reservation);
    }

    public List<FindAvailableTimesResponse> getAvailableTimes(final LocalDate date, final Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        return reservationTimes.stream()
                .map(reservationTime -> generateFindAvailableTimesResponse(reservations, reservationTime))
                .toList();
    }

    private static FindAvailableTimesResponse generateFindAvailableTimesResponse(final List<Reservation> reservations,
            final ReservationTime reservationTime) {
        return FindAvailableTimesResponse.of(
                reservationTime.getId(),
                reservationTime.getTime(),
                reservations.stream()
                        .anyMatch(reservation -> reservation.isSameTime(reservationTime)));
    }

    public void deleteReservation(final Long id) {
        reservationRepository.deleteById(id);
    }
}
