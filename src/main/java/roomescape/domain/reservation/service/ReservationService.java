package roomescape.domain.reservation.service;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.ConflictException;
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

    private final Clock clock;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(Clock clock, ReservationRepository reservationRepository, TimeRepository timeRepository,
        ThemeRepository themeRepository) {
        this.clock = clock;
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
            .map(ReservationResponseDto::from)
            .toList();
    }

    public ReservationCreateResponseDto saveReservation(ReservationCreateRequestDto requestDto) {
        Reservation reservation = createReservation(requestDto);
        validateDuplicates(requestDto);
        return ReservationCreateResponseDto.from(reservationRepository.save(reservation));
    }

    private void validateDuplicates(ReservationCreateRequestDto requestDto) {
        Optional<Reservation> duplicatedReservation =
            reservationRepository.findReservationByDateTimeAndThemeId(requestDto.date(), requestDto.timeId(), requestDto.themeId());

        if (duplicatedReservation.isPresent()) {
            throw new ConflictException(ErrorCode.RESERVATION_DUPLICATE);
        }
    }

    private Reservation createReservation(ReservationCreateRequestDto requestDto) {
        Time time = timeRepository.findTimeById(requestDto.timeId())
            .orElseThrow(() -> new BadRequestException(ErrorCode.TIME_NOT_FOUND, List.of()));
        Theme theme = themeRepository.findThemeById(requestDto.themeId())
            .orElseThrow(() -> new BadRequestException(ErrorCode.THEME_NOT_FOUND, List.of()));
        return Reservation.create(requestDto.name(), requestDto.date(), time, theme, clock);
    }

    public void deleteReservationById(Long id) {
        if (reservationRepository.deleteReservationById(id) == 0) {
            throw new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }
}
