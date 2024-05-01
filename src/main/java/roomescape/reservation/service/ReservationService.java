package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.TimeResponse;
import roomescape.time.repository.ReservationTimeRepository;

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

    public Long save(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

        Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        if (LocalDate.now().isAfter(reservationRequest.date())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }

        Reservation reservation = new Reservation(new Name(reservationRequest.name()),
                reservationRequest.date(), theme,
                reservationTime);

        if (reservationRepository.existReservation(reservation)) {
            throw new IllegalArgumentException("중복된 예약이 있습니다.");
        }

        return reservationRepository.save(reservation);
    }

    public ReservationResponse findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        TimeResponse timeResponse = new TimeResponse(reservation.getTime().getId(),
                reservation.getTime().getStartAt());

        ThemeResponse themeResponse = new ThemeResponse(reservation.getTheme().getId(),
                reservation.getTheme().getName(), reservation.getTheme().getDescription(),
                reservation.getTheme().getThumbnail());

        return new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getDate(), themeResponse, timeResponse);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> {
                    TimeResponse timeResponse = new TimeResponse(reservation.getTime().getId(),
                            reservation.getTime().getStartAt());
                    ThemeResponse themeResponse = new ThemeResponse(reservation.getTheme().getId(),
                            reservation.getTheme().getName(), reservation.getTheme().getDescription(),
                            reservation.getTheme().getThumbnail());
                    return new ReservationResponse(reservation.getId(),
                            reservation.getName(), reservation.getDate(), themeResponse, timeResponse);
                })
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
