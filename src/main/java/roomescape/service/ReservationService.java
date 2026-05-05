package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRespository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    private final ThemeRespository themeRespository;

    public ReservationService(ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRespository themeRespository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRespository = themeRespository;
    }

    public List<ReservationResponseDTO> readAllReservation() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponseDTO::from)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> readReservationByTimeId(Long timeId) {
        return reservationRepository.findByTimeId(timeId).stream()
                .map(ReservationResponseDTO::from).collect(
                        Collectors.toList());
    }

    public ReservationResponseDTO addReservation(ReservationRequestDTO reservationRequestDTO) {
        ReservationTime time = reservationTimeRepository.findById(reservationRequestDTO.timeId())
                .orElseThrow(
                        () -> new RuntimeException("존재하지 않는 시간입니다."));
        Theme theme = themeRespository.findById(reservationRequestDTO.themeId())
                .orElseThrow(
                        () -> new RuntimeException("존재하지 않는 테마입니다."));

        Reservation reservation = new Reservation(reservationRequestDTO.name(),
                reservationRequestDTO.date(), time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);
        ReservationResponseDTO response = ReservationResponseDTO.from(savedReservation);
        return response;
    }


    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }
}
