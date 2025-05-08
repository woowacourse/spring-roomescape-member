package roomescape.reservation.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationRequestDto;
import roomescape.reservation.domain.dto.ReservationResponseDto;
import roomescape.reservation.exception.InvalidReservationTimeException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;
import roomescape.reservationTime.exception.DuplicateReservationException;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeResponseDto;
import roomescape.theme.exception.InvalidThemeException;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository repository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.repository = repository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponseDto> findAll() {
        List<Reservation> reservations = repository.findAll();
        return reservations.stream()
                .map(this::convertReservationResponseDto)
                .collect(Collectors.toList());
    }

    public ReservationResponseDto add(ReservationRequestDto requestDto) {
        Reservation reservation = convertReservation(requestDto);
        validateDuplicateDateTime(reservation);
        Reservation savedReservation = repository.add(reservation);
        return convertReservationResponseDto(savedReservation);
    }

    public void delete(Long id) {
        repository.findByIdOrThrow(id);
        repository.delete(id);
    }

    private void validateDuplicateDateTime(Reservation inputReservation) {
        boolean exists = repository.existsByDateAndTime(
                inputReservation.getDate(),
                inputReservation.getReservationTime()
        );
        if (exists) {
            throw new DuplicateReservationException("이미 예약되어 있는 시간입니다.");
        }
    }

    private Reservation convertReservation(ReservationRequestDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 예약 시간입니다."));
        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마입니다."));

        return dto.toEntity(reservationTime, theme);
    }

    private ReservationResponseDto convertReservationResponseDto(Reservation reservation) {
        ReservationTimeResponseDto reservationTimeResponseDto = ReservationTimeResponseDto.of(
                reservation.getReservationTime());
        ThemeResponseDto themeResponseDto = ThemeResponseDto.of(reservation.getTheme());
        return ReservationResponseDto.from(reservation, reservationTimeResponseDto, themeResponseDto);
    }
}
