package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.NotFoundException;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

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
            .map(Reservation::toResponseDto)
            .toList();
    }

    public ReservationCreateResponseDto saveReservation(ReservationCreateRequestDto requestDto) {
        Reservation reservation = createReservation(requestDto);
        return reservationRepository.save(reservation).toCreateResponseDto();
    }

    private Reservation createReservation(ReservationCreateRequestDto requestDto) {
        Time time = timeRepository.findTimeById(requestDto.timeId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findThemeById(requestDto.themeId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
        return Reservation.create(requestDto.name(), requestDto.date(), time, theme);
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteReservationById(id);
    }
}
