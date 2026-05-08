package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ThemeResponse;
import roomescape.dto.TimeResponse;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository, TimeService timeService,
                              ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeService = themeService;
    }

    public List<ReservationResponse> read() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public void removeById(Long id) {
        int deleteCnt = reservationRepository.deleteById(id);
        if(deleteCnt == 0) {
            throw new IllegalArgumentException("존재하지 않는 예약의 ID 입니다.");
        }
    }

    public ReservationResponse register(ReservationRequest reservationRequest) {
        TimeResponse timeResponse = timeService.readById(reservationRequest.timeId());
        ThemeResponse themeResponse = themeService.readById(reservationRequest.themeId());

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 예약입니다.");
        }

        Reservation reservation = reservationRepository.save(reservationRequest, timeResponse, themeResponse);
        return ReservationResponse.from(reservation);
    }

}
