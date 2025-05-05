package roomescape.reservation.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.globalException.BadRequestException;
import roomescape.globalException.ConflictException;
import roomescape.reservation.ReservationMapper;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationRequestDto;
import roomescape.reservation.domain.dto.ReservationResponseDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.ReservationTimeMapper;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.ThemeMapper;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeResponseDto;
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
            throw new ConflictException("이미 예약되어 있는 시간입니다.");
        }
    }

    private Reservation convertReservation(ReservationRequestDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 예약 시간입니다."));
        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 테마입니다."));

        return ReservationMapper.toEntity(dto, reservationTime, theme);
    }

    private ReservationResponseDto convertReservationResponseDto(Reservation reservation) {
        ReservationTimeResponseDto reservationTimeResponseDto = ReservationTimeMapper.toResponseDto(
                reservation.getReservationTime());
        ThemeResponseDto themeResponseDto = ThemeMapper.toResponseDto(reservation.getTheme());
        return ReservationMapper.toResponseDto(reservation, reservationTimeResponseDto, themeResponseDto);
    }
}
