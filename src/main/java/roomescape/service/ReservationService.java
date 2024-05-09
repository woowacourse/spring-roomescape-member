package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.Theme.Theme;
import roomescape.domain.member.Member;
import roomescape.dto.LoginMember;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
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

    public Long addReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        ReservationTime reservationTime = findReservationTime(reservationRequest.timeId());
        Theme theme = findTheme(reservationRequest.themeId());
        Member member = findMember(loginMember.id());

        validateAddable(reservationRequest, reservationTime);
        Reservation reservationToSave = reservationRequest.toEntity(reservationTime, theme, member);
        return reservationRepository.save(reservationToSave);
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

    private void validateAddable(ReservationRequest reservationRequest, ReservationTime reservationTime) {
        validateReservationNotDuplicate(reservationRequest);
        validateUnPassedDate(reservationRequest.date(), reservationTime.getStartAt());
    }

    private void validateReservationNotDuplicate(ReservationRequest reservationRequest) {
        if (reservationRepository.existDateTimeAndTheme(
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId())
        ) {
            throw new ClientErrorExceptionWithLog(
                    "[ERROR] 해당 시간에 동일한 테마가 예약되어있어 예약이 불가능합니다.",
                    "생성 예약 정보 : " + reservationRequest
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
