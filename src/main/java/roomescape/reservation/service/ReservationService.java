package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.admin.dto.ReservationFilterRequest;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.ReservationAdminRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

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

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findReservationsBy(ReservationFilterRequest reservationFilterRequest) {
        LocalDate dateFrom = reservationFilterRequest.dateFrom();
        LocalDate dateTo = reservationFilterRequest.dateTo();
        validateDate(dateFrom, dateTo);
        return reservationRepository.findBy(reservationFilterRequest.themeId(), reservationFilterRequest.themeId(),
                        dateFrom, dateTo).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(ReservationRequest reservationRequest, LoginMember member) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId());
        Theme theme = themeRepository.findById(reservationRequest.themeId());
        LocalDate date = LocalDate.parse(reservationRequest.date().getDate());

        validateTimeExist(reservationTime, reservationRequest.timeId());
        validateThemeExist(theme, reservationRequest.themeId());
        validateReservationDuplicate(date, reservationRequest.timeId(), reservationRequest.themeId());

        return saveReservation(date, reservationTime, theme, member.id());
    }

    public ReservationResponse createByAdmin(ReservationAdminRequest reservationAdminRequest) {
        ReservationRequest request = new ReservationRequest(reservationAdminRequest.date(),
                reservationAdminRequest.timeId(), reservationAdminRequest.themeId());
        LoginMember loginMember = new LoginMember(reservationAdminRequest.memberId());
        return create(request, loginMember);
    }

    public void delete(long reservationId) {
        if (!reservationRepository.deleteById(reservationId)) {
            throw new IllegalArgumentException(String.format("잘못된 예약입니다. id=%d를 확인해주세요.", reservationId));
        }
    }

    private ReservationResponse saveReservation(LocalDate date, ReservationTime reservationTime, Theme theme,
                                                long memberId) {
        Reservation reservation = reservationRepository.save(
                new Reservation(date, reservationTime, theme));
        reservationRepository.saveReservationList(memberId, reservation.getId());
        Member member = memberRepository.findById(memberId);
        return ReservationResponse.of(reservation, member);
    }

    private void validateTimeExist(ReservationTime reservationTime, long timeId) {
        if (reservationTime == null) {
            throw new IllegalArgumentException(
                    String.format("잘못된 예약 시간입니다. id=%d를 확인해주세요.", timeId));
        }
    }

    private void validateThemeExist(Theme theme, long themeId) {
        if (theme == null) {
            throw new IllegalArgumentException(
                    String.format("잘못된 테마입니다. id=%d를 확인해주세요.", themeId));
        }
    }

    private void validateReservationDuplicate(LocalDate date, long timeId, long themeId) {
        if (reservationRepository.existBy(date, timeId, themeId)) {
            throw new IllegalArgumentException("예약 시간이 중복되었습니다.");
        }
    }

    private void validateDate(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException("끝 날짜가 시작 날짜보다 전일 수 없습니다.");
        }
    }
}
