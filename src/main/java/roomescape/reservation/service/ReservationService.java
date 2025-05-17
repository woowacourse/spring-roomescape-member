package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.controller.dto.AvailableTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse add(ReservationRequest request) {
        Reservation reservation = createReservationWithoutId(request);
        Long id = reservationRepository.saveAndReturnId(reservation);
        return ReservationResponse.from(reservation.createWithId(id));
    }

    public void remove(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        List<ReservationTime> times = reservationTimeRepository.findAll();
        return convertTimeToResponses(reservations, times);
    }

    public List<ReservationResponse> getFilteredReservations(Long themeId, Long memberId, LocalDate dateFrom,
                                                             LocalDate dateTo) {
        return reservationRepository.findAllByThemeIdAndMemberIdAndPeriod(themeId, memberId, dateFrom, dateTo).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private List<AvailableTimeResponse> convertTimeToResponses(List<Reservation> reservations,
                                                               List<ReservationTime> times) {
        return times.stream()
                .map(time -> AvailableTimeResponse.from(time.getStartAt(), time.getId(),
                        isAlreadyBooked(time.getId(), reservations)))
                .toList();
    }

    private Reservation createReservationWithoutId(ReservationRequest request) {
        ReservationTime findTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalStateException("해당하는 시간 정보가 존재하지 않습니다."));
        Theme findTheme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Member findMember = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Reservation reservation = request.toReservationWithoutId(findMember, findTime, findTheme);

        validateDuplicateReservation(request.date(), request.timeId(), request.themeId());
        reservation.validateNotPast(LocalDate.now(), LocalTime.now());

        return reservation;
    }

    private Boolean isAlreadyBooked(Long timeId, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameId(timeId));
    }

    private void validateDuplicateReservation(LocalDate localDate, Long timeId, Long themeId) {
        if (reservationRepository.existReservationByDateAndTimeIdAndThemeId(localDate, timeId, themeId)) {
            throw new IllegalArgumentException("해당 날짜, 시간, 테마에 대한 동일한 예약이 존재합니다.");
        }
    }

}
