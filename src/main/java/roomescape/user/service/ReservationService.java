package roomescape.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import roomescape.admin.domain.Theme;
import roomescape.admin.repository.AdminThemeRepository;
import roomescape.user.domain.Reservation;
import roomescape.user.domain.ReservationTime;
import roomescape.user.dto.ReservationRequest;
import roomescape.user.dto.ReservationResponse;
import roomescape.user.dto.TimeResponse;
import roomescape.user.repository.ReservationRepository;
import roomescape.user.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final AdminThemeRepository adminThemeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository, AdminThemeRepository adminThemeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.adminThemeRepository = adminThemeRepository;
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "[ERROR] 존재하지 않는 time id입니다.")
                );
        Theme theme = adminThemeRepository.findById(request.themeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "[ERROR] 존재하지 않는 theme id입니다.")
                );
        Reservation reservation = Reservation.of(
                request.name(),
                request.date(),
                time,
                theme
        );
        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    public List<TimeResponse> getReservations(LocalDate date, Long themeId) {
        List<Long> bookedTimeIds = reservationRepository.findTimeByDateAndThemeId(date, themeId);
        return reservationTimeRepository.findAll().stream()
                .filter(time -> !bookedTimeIds.contains(time.getId()))
                .map(TimeResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
