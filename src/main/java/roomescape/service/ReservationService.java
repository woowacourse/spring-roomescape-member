package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> read() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public void removeById(Long id) {
        try {
            reservationRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("삭제하고자 하는 예약 ID가 없습니다.");
        }
        reservationRepository.deleteById(id);
    }

    public ReservationResponse register(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = timeRepository.findById(reservationRequest.timeId());
        Theme theme = themeRepository.findById(reservationRequest.themeId());

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 예약입니다.");
        }

        Reservation reservation = reservationRepository.save(reservationRequest.name(), reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId(), reservationTime, theme);
        return ReservationResponse.from(reservation);
    }

}
