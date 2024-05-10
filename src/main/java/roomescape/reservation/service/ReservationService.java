package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import roomescape.auth.config.AuthInfo;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

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

    public CreateReservationResponse createReservation(final AuthInfo authInfo,
                                                       final CreateReservationRequest createReservationRequest) {
        ReservationTime reservationTime = findReservationTime(createReservationRequest.timeId());
        checkDateTimeToCreateIsPast(createReservationRequest.date(), reservationTime);

        Theme theme = findTheme(createReservationRequest.themeId());
        Member member = findMember(authInfo.getMemberId());
        Reservation reservation = createReservationRequest.toReservation(member, reservationTime, theme);
        // TODO: 얘가 42줄이여도 될듯
        validateAlreadyExistReservation(reservationTime, theme, reservation);

        return CreateReservationResponse.from(reservationRepository.save(reservation));
    }

    private ReservationTime findReservationTime(final Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("생성하려는 예약의 예약 시간이 존재하지 않습니다."));
    }

    private Theme findTheme(final Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("생성하려는 테마가 존재하지 않습니다."));
    }

    private Member findMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""));
    }

    private void checkDateTimeToCreateIsPast(final LocalDate dateToCreate, final ReservationTime reservationTime) {
        LocalDate now = LocalDate.now();
        checkDateToCreateIsPast(dateToCreate, now);
        checkTimeToCreateIsPastWhenSameDate(dateToCreate, now, reservationTime);
    }

    private void checkDateToCreateIsPast(final LocalDate dateToCreate, final LocalDate now) {
        if (dateToCreate.isBefore(now)) {
            throw new IllegalArgumentException("지나간 날짜에 대한 예약 생성은 불가능합니다.");
        }
    }

    private void checkTimeToCreateIsPastWhenSameDate(final LocalDate dateToCreate,
                                                     final LocalDate now,
                                                     final ReservationTime timeToCreate) {
        if (dateToCreate.isEqual(now) && timeToCreate.isNotAfter(LocalTime.now())) {
            throw new IllegalArgumentException("지나간 시간 또는 현재 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    private void validateAlreadyExistReservation(final ReservationTime reservationTime, final Theme theme,
                                                 final Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeAndTheme(reservation.getDate(), reservationTime.getId(),
                theme.getId())) {
            throw new IllegalStateException("동일한 시간의 예약이 존재합니다.");
        }
    }

    public List<FindReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(FindReservationResponse::from)
                .toList();
    }

    public FindReservationResponse getReservation(final Long id) {
        Reservation reservation = findReservation(id);
        return FindReservationResponse.from(reservation);
    }

    private Reservation findReservation(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("조회하려는 예약이 존재하지 않습니다."));
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
        return FindAvailableTimesResponse.from(
                reservationTime,
                reservations.stream()
                        .anyMatch(reservation -> reservation.isSameTime(reservationTime)));
    }

    public void deleteReservation(final Long id) {
        validateExistReservation(id);
        reservationRepository.deleteById(id);
    }

    private void validateExistReservation(final Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NoSuchElementException("삭제하려는 예약이 존재하지 않습니다.");
        }
    }
}
