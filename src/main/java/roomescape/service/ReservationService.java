package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.Theme.Theme;
import roomescape.domain.member.Member;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.ClientErrorExceptionWithLog;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.util.Date;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public Long addReservation(ReservationRequest request) {
        Reservation reservation = convertReservation(request);
        validateAddable(reservation);
        return reservationRepository.save(reservation);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse getReservation(Long id) {
        Reservation reservation = findReservationById(id);
        return ReservationResponse.from(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = findReservationById(id);
        reservationRepository.delete(reservation.getId());
    }

    private Reservation convertReservation(ReservationRequest request) {
        ReservationTime reservationTime = findReservationTime(request.timeId());
        Theme theme = findTheme(request.themeId());
        Member member = findMember(request.memberId());
        return request.toEntity(reservationTime, theme, member);
    }

    private Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ClientErrorExceptionWithLog(
                        "[ERROR] 잘못된 예약 정보 입니다.",
                        "reservation_id : " + id
                ));
    }

    private ReservationTime findReservationTime(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new ClientErrorExceptionWithLog(
                        "[ERROR] 잘못된 예약시간 정보 입니다.",
                        "time_id : " + timeId
                ));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ClientErrorExceptionWithLog(
                        "[ERROR] 잘못된 테마 정보 입니다.",
                        "theme_id : " + themeId
                ));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ClientErrorExceptionWithLog(
                        "[ERROR] 잘못된 사용자 정보 입니다.",
                        "member_id : " + memberId
                ));
    }

    private void validateAddable(Reservation reservation) {
        validateReservationNotDuplicate(reservation);
        validateUnPassedDate(reservation.getDate(), reservation.getTime().getStartAt());
    }

    private void validateReservationNotDuplicate(Reservation reservation) {
        if (reservationRepository.existDateTimeAndTheme(
                reservation.getDate(),
                reservation.getTimeId(),
                reservation.getThemeId())
        ) {
            throw new ClientErrorExceptionWithLog(
                    "[ERROR] 해당 시간에 동일한 테마가 예약되어있어 예약이 불가능합니다.",
                    "생성 예약 정보 : " + reservation
            );
        }
    }

    private void validateUnPassedDate(LocalDate date, LocalTime time) {
        if (Date.isPastDateTime(date, time)) {
            throw new ClientErrorExceptionWithLog(
                    "[ERROR] 지나간 날짜와 시간은 예약이 불가능합니다.",
                    "생성 예약 시간 : " + date + " " + time
            );
        }
    }
}
