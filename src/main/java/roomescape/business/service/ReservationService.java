package roomescape.business.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationDateTime;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.business.domain.theme.Theme;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;
import roomescape.presentation.exception.BadRequestException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest, final Member member) {
        final Reservation reservation = convertToReservation(reservationRequest, member);
        final Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void cancelReservationById(final long id) {
        reservationRepository.deleteById(id);
    }

    private Reservation convertToReservation(final ReservationRequest reservationRequest, final Member member) {
        final Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new BadRequestException("테마가 존재하지 않습니다."));
        final ReservationTime time = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new BadRequestException("예약 시간이 존재하지 않습니다."));
        final ReservationDateTime dateTime = new ReservationDateTime(reservationRequest.date(), time);
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("지나간 날짜와 시간은 예약 불가합니다.");
        }
        if (reservationRepository.existsByDateAndTimeAndTheme(dateTime.getDate(), dateTime.getTimeId(),
                theme.getId())) {
            throw new BadRequestException("해당 시간에 이미 예약이 존재합니다.");
        }
        return Reservation.register(member.getName(), reservationRequest.date(), time, theme);
    }
}
