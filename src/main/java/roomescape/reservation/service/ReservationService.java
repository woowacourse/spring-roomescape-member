package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.dto.TimeResponse;
import roomescape.reservation.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public Long save(ReservationCreateRequest reservationCreateRequest) {
        TimeResponse timeResponse = reservationTimeService.findById(reservationCreateRequest.timeId());
        ThemeResponse themeResponse = themeService.findById(reservationCreateRequest.themeId());
        getValidatedReservation(reservationCreateRequest);

        Theme theme = new Theme(themeResponse.id(), new Name(themeResponse.name()), themeResponse.description(),
                themeResponse.thumbnail());
        ReservationTime reservationTime = new ReservationTime(timeResponse.id(), timeResponse.startAt());
        Reservation reservation = reservationCreateRequest.toReservation(theme, reservationTime);

        return reservationRepository.save(reservation);
    }

    private void getValidatedReservation(ReservationCreateRequest reservationCreateRequest) {
        if (LocalDate.now().isAfter(reservationCreateRequest.date())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }

        if (reservationTimeService.isExist(reservationCreateRequest.timeId())) {
            throw new IllegalArgumentException("중복된 예약이 있습니다.");
        }
    }

    public ReservationResponse findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        return ReservationResponse.toResponse(reservation);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
