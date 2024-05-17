package roomescape.reservation.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.global.exception.exceptions.InvalidInputException;
import roomescape.global.exception.exceptions.NotExistingEntryException;
import roomescape.global.exception.exceptions.ReservingPastTimeException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.dto.AvailableTimeResponse;
import roomescape.reservation.dto.AvailableTimeResponses;
import roomescape.reservation.dto.ReservationAdminCreateRequest;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationResponses;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponses findAll() {
        List<ReservationResponse> reservationResponses = reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
        return new ReservationResponses(reservationResponses);
    }

    public ReservationResponse findByReservationId(Long id) {
        Reservation reservation = reservationRepository.findByReservationId(id);
        return ReservationResponse.from(reservation);
    }

    public AvailableTimeResponses findByDateAndThemeId(LocalDate date, Long themeId) {
        List<Long> foundReservations = reservationRepository
                .findByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getReservationTime)
                .map(ReservationTime::getId)
                .toList();
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<AvailableTimeResponse> availableTimeResponses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            availableTimeResponses.add(AvailableTimeResponse.from(
                    reservationTime.getId(),
                    reservationTime.getStartAt(),
                    foundReservations.contains(reservationTime.getId())
            ));
        }
        return new AvailableTimeResponses(availableTimeResponses);
    }

    public ReservationResponses findByIdsAndDates(Long memberId, Long themeId, LocalDate from, LocalDate to) {
        List<ReservationResponse> reservationResponses
                = reservationRepository.findByIdsAndDates(memberId, themeId, from, to)
                .stream()
                .map(ReservationResponse::from)
                .toList();
        return new ReservationResponses(reservationResponses);
    }

    public ReservationResponse create(ReservationCreateRequest reservationCreateRequest, LoginMember loginMember) {
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(reservationCreateRequest.timeId());
        validateAvailableDateTime(reservationCreateRequest.date(), reservationTime.getStartAt());
        Theme theme = themeRepository.findByThemeId(reservationCreateRequest.themeId());
        Member member = memberRepository.findByMemberName(loginMember.name());
        Reservation reservation = new Reservation(
                reservationCreateRequest.date(),
                reservationTime,
                theme,
                member
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public ReservationResponse createByAdmin(ReservationAdminCreateRequest reservationAdminCreateRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(
                reservationAdminCreateRequest.timeId());
        validateAvailableDateTime(reservationAdminCreateRequest.date(), reservationTime.getStartAt());
        Theme theme = themeRepository.findByThemeId(reservationAdminCreateRequest.themeId());
        Member member = memberRepository.findByMemberId(reservationAdminCreateRequest.memberId());
        Reservation reservation = new Reservation(
                reservationAdminCreateRequest.date(),
                reservationTime,
                theme,
                member
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateAvailableDateTime(LocalDate date, LocalTime time) {
        if (date == null) {
            throw new InvalidInputException("예약 날짜가 입력되지 않았습니다.");
        }
        if (time == null) {
            throw new InvalidInputException("예약 시간이 입력되지 않았습니다.");
        }
        LocalDate nowDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalTime nowTime = LocalTime.now(ZoneId.of("Asia/Seoul"));
        if (date.isBefore(nowDate) || (date.isEqual(nowDate) && time.isBefore(nowTime))) {
            throw new ReservingPastTimeException("과거의 날짜 또는 시간은 예약할 수 없습니다. 현재 시간 이후로 예약해주세요.");
        }
    }

    public void delete(Long id) {
        if (reservationRepository.deleteById(id) == 0) {
            throw new NotExistingEntryException("삭제할 예약이 존재하지 않습니다");
        }
    }
}
