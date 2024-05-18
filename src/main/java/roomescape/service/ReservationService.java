package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

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

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<AvailableTimeResponse> findByDateAndThemeId(LocalDate date, Long themeId) {
        List<Long> foundReservations = reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(reservation -> reservation.getReservationTime().getId())
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
        return availableTimeResponses;
    }

    public ReservationResponse createUserReservation(ReservationCreateRequest reservationCreateRequest,
                                                     LoginMember member) {
        Long timeId = reservationCreateRequest.timeId();
        Long themeId = reservationCreateRequest.themeId();
        LocalDate date = reservationCreateRequest.date();
        return createReservation(member.id(), timeId, themeId, date);
    }

    public ReservationResponse createAdminReservation(ReservationAdminCreateRequest reservationAdminCreateRequest) {
        Long memberId = reservationAdminCreateRequest.memberId();
        Long timeId = reservationAdminCreateRequest.timeId();
        Long themeId = reservationAdminCreateRequest.themeId();
        LocalDate date = reservationAdminCreateRequest.date();
        return createReservation(memberId, timeId, themeId, date);
    }

    private ReservationResponse createReservation(Long memberId, Long timeId, Long themeId, LocalDate date) {
        Member member = memberRepository.findById(memberId);
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(timeId);
        validateAvailableDateTime(date, reservationTime.getStartAt());
        Theme theme = themeRepository.findByThemeId(themeId);
        Reservation reservation = new Reservation(
                member,
                date,
                reservationTime,
                theme
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateAvailableDateTime(LocalDate date, LocalTime time) {
        LocalDate nowDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalTime nowTime = LocalTime.now(ZoneId.of("Asia/Seoul"));

        if (date.isBefore(nowDate) || (date.isEqual(nowDate) && time.isBefore(nowTime))) {
            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다.");
        }
    }

    public void delete(Long id) {
        if (reservationRepository.deleteById(id) == 0) {
            throw new IllegalArgumentException("삭제할 예약이 존재하지 않습니다");
        }
    }

    public List<ReservationResponse> findSearchedReservations(
            Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        return reservationRepository.findSearchedReservation(themeId, memberId, dateFrom, dateTo).stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
