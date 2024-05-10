package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.LoginMemberRepository;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.LoginMember;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.ReservationReadRequest;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final LoginMemberRepository loginMemberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              LoginMemberRepository loginMemberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.loginMemberRepository = loginMemberRepository;
    }

    public ReservationResponse create(final AdminReservationRequest adminReservationRequest) {
        Theme theme = findThemeById(adminReservationRequest.themeId());
        ReservationTime reservationTime = findTimeById(adminReservationRequest.timeId());
        LoginMember loginMember = findLoginMemberById(adminReservationRequest.memberId());
        Reservation reservation = adminReservationRequest.toReservation(theme, reservationTime, loginMember);
        Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    public ReservationResponse create(final ReservationRequest reservationRequest, LoginMember loginMember) {
        Theme theme = findThemeById(reservationRequest.themeId());
        ReservationTime reservationTime = findTimeById(reservationRequest.timeId());
        Reservation reservation = reservationRequest.toReservation(theme, reservationTime, loginMember);
        Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    private ReservationTime findTimeById(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 시간입니다."));
    }

    private Theme findThemeById(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 테마입니다."));
    }

    private LoginMember findLoginMemberById(long memberId) {
        return loginMemberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 사용자입니다."));
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findByThemeAndMemberAndDate(ReservationReadRequest reservationReadRequest) {
        return reservationRepository.findByThemeAndMemberAndDate(reservationReadRequest.themeId(),
                        reservationReadRequest.memberId(), reservationReadRequest.dateFrom(), reservationReadRequest.dateTo())
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
