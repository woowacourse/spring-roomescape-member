package roomescape.domain.reservation.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.error.type.ReservationErrorType;
import roomescape.domain.reservation.mapper.ReservationMapper;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.global.error.dto.ParameterErrorResponseDto;
import roomescape.global.error.exception.GeneralException;
import roomescape.global.error.exception.GeneralNotFoundException;

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

    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        return convertReservationsToDto(reservations);
    }

    private List<ReservationResponseDto> convertReservationsToDto(List<Reservation> reservations) {
        return reservations.stream()
            .map(ReservationMapper::toResponseDto)
            .toList();
    }

    public ReservationCreateResponseDto saveReservation(ReservationCreateRequestDto requestDto) {
        Reservation reservation = createReservation(requestDto);

        if (reservationRepository.existsReservationByDateAndTimeAndTheme(reservation.getDate(), reservation.getTime(),
            reservation.getTheme())) {
            throw new GeneralException(ReservationErrorType.ALREADY_RESERVED);
        }

        return ReservationMapper.toCreateResponseDto(reservationRepository.save(reservation));
    }

    private Reservation createReservation(ReservationCreateRequestDto requestDto) {

        List<ParameterErrorResponseDto> parameterErrorResponses = new ArrayList<>();

        Time time = timeRepository.findTimeById(requestDto.timeId()).orElse(null);
        if (time == null) {
            parameterErrorResponses.add(new ParameterErrorResponseDto("timeId", "존재 하지 않는 시간대입니다."));
        }

        Theme theme = themeRepository.findThemeById(requestDto.themeId()).orElse(null);
        if (theme == null) {
            parameterErrorResponses.add(new ParameterErrorResponseDto("themeId", "존재 하지 않는 테마입니다."));
        }

        if (!parameterErrorResponses.isEmpty()) {
            throw new GeneralNotFoundException(ReservationErrorType.FIELD_RESOURCE_NOT_FOUND, parameterErrorResponses);
        }

        return Reservation.create(requestDto.name(), requestDto.date(), time, theme);
    }

    public void deleteReservationById(Long id) {
        if (!reservationRepository.existsReservationById(id)) {
            throw new GeneralException(ReservationErrorType.RESERVATION_NOT_FOUND);
        }

        reservationRepository.deleteReservationById(id);
    }
}
