package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.FindReservedTimeParams;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse reserve(CreateReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.getTimeId());
        Theme theme = themeRepository.findById(request.getThemeId());

        List<Long> reservedIds = reservationTimeRepository.findIdByCondition(new FindReservedTimeParams(theme.getId(), request.getDate()));
        if(reservedIds.contains(request.getTimeId())) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
        CreateReservationParams params = new CreateReservationParams(request.getName(), request.getDate(),
                time.getId(), theme.getId());
        Reservation reservation = reservationRepository.save(params);

        return ReservationResponse.from(reservation);
    }

    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
