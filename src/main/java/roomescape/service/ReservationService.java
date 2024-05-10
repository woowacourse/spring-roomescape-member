package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.dto.request.ReservationAddMemberRequest;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.reservation.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeService reservationTimeService, ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public ReservationResponse addReservation(ReservationAddRequest reservationAddRequest, ReservationAddMemberRequest reservationAddMemberRequest) {
        ReservationTimeResponse timeResponse = reservationTimeService.getTime(reservationAddRequest.timeId());
        ThemeResponse themeResponse = themeService.getTheme(reservationAddRequest.themeId());
        validateAddReservation(reservationAddRequest, LocalDateTime.of(reservationAddRequest.date(), timeResponse.toReservationTime().getStartAt()));

        Reservation reservation = new Reservation(
                new Member(reservationAddMemberRequest.id(), new Name(reservationAddMemberRequest.name()), new Email(reservationAddMemberRequest.email()), null, null),
                reservationAddRequest.date(),
                timeResponse.toReservationTime(),
                themeResponse.toTheme()
        );
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    private void validateAddReservation(ReservationAddRequest reservationAddRequest, LocalDateTime reservationTime) {
        LocalDateTime now = LocalDateTime.now();

        if (reservationTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 시간은 예약할 수 없습니다.");
        }
        if (reservationRepository.hasSameReservation(reservationAddRequest.getStringDate(), reservationAddRequest.timeId(), reservationAddRequest.themeId())) {
            throw new IllegalArgumentException("중복 예약을 할 수 없습니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    public List<ReservationResponse> findReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> findReservationsByCondition(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        return reservationRepository.findByCondition(memberId, themeId, dateFrom, dateTo)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
