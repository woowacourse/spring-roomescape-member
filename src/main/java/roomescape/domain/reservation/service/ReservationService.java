package roomescape.domain.reservation.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDTO;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDTO;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.reservation.error.exception.ReservationNotFoundException;
import roomescape.domain.reservation.error.type.ReservationErrorType;
import roomescape.domain.reservation.mapper.ReservationMapper;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.global.error.exception.dto.FieldErrorResponseDTO;

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

    public List<ReservationResponseDTO> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        return convertReservationsToDTO(reservations);
    }

    private List<ReservationResponseDTO> convertReservationsToDTO(List<Reservation> reservations) {
        return reservations.stream()
            .map(ReservationMapper::toResponseDTO)
            .toList();
    }

    public ReservationCreateResponseDTO saveReservation(ReservationCreateRequestDTO requestDTO) {
        Reservation reservation = createReservation(requestDTO);

        if (reservationRepository.existsReservationByDateAndTimeAndTheme(reservation.getDate(), reservation.getTime(),
            reservation.getTheme())) {
            throw new ReservationException(ReservationErrorType.ALREADY_RESERVED);
        }

        return ReservationMapper.toCreateResponseDTO(reservationRepository.save(reservation));
    }

    private Reservation createReservation(ReservationCreateRequestDTO requestDTO) {

        List<FieldErrorResponseDTO> fieldErrorResponses = new ArrayList<>();

        Time time = timeRepository.findTimeById(requestDTO.timeId()).orElse(null);
        if (time == null) {
            fieldErrorResponses.add(new FieldErrorResponseDTO("timeId", "존재 하지 않는 시간대입니다."));
        }

        Theme theme = themeRepository.findThemeById(requestDTO.themeId()).orElse(null);
        if (theme == null) {
            fieldErrorResponses.add(new FieldErrorResponseDTO("themeId", "존재 하지 않는 테마입니다."));
        }

        if (!fieldErrorResponses.isEmpty()) {
            throw new ReservationNotFoundException(ReservationErrorType.FIELD_RESOURCE_NOT_FOUND, fieldErrorResponses);
        }

        return Reservation.create(requestDTO.name(), requestDTO.date(), time, theme);
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteReservationById(id);
    }
}
