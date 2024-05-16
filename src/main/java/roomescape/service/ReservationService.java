package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.ReservationAdminRequest;
import roomescape.service.dto.ReservationCookieRequest;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationSpecificRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAllReservations()
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse createCookieReservation(ReservationCookieRequest request, Member member) {
        Reservation reservation = request.toReservation(member);
        return createReservation(reservation);
    }

    public ReservationResponse createAdminReservation(ReservationAdminRequest request) {
        Reservation reservation = request.toReservation();
        return createReservation(reservation);
    }

    private ReservationResponse createReservation(Reservation reservation) {
        Long timeId = reservation.getTimeId();
        if (!reservationTimeRepository.isExistTimeOf(timeId)) {
            throw new IllegalArgumentException("예약 하려는 시간이 저장되어 있지 않습니다.");
        }

        ReservationTime time = reservationTimeRepository.findReservationTimeById(timeId);
        if (isBeforeNow(reservation, time)) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약은 불가능합니다.");
        }

        if (reservationRepository.hasSameReservationForThemeAtDateTime(reservation)) {
            throw new IllegalArgumentException("해당 테마는 같은 시간에 이미 예약이 존재합니다.");
        }

        Reservation savedReservation = reservationRepository.insertReservation(reservation);
        return new ReservationResponse(savedReservation);
    }

    public void deleteReservation(long id) {
        if (!reservationRepository.isExistReservationOf(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        reservationRepository.deleteReservationById(id);
    }

    private boolean isBeforeNow(Reservation reservation, ReservationTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), time.getStartAt());
        return reservationDateTime.isBefore(LocalDateTime.now());
    }

    public List<ReservationResponse> findSpecificReservations(ReservationSpecificRequest request) {
        return reservationRepository.findSpecificReservations(request)
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
