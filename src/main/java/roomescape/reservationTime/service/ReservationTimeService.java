package roomescape.reservationTime.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.AvailableReservationTimeResponseDto;
import roomescape.reservationTime.domain.dto.ReservationTimeRequestDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;
import roomescape.reservationTime.exception.AlreadyReservedTimeException;
import roomescape.reservationTime.exception.DuplicateReservationException;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.InvalidThemeException;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository repository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(
            ReservationTimeRepository repository,
            ReservationRepository reservationRepository,
            ThemeRepository themeRepository
    ) {
        this.repository = repository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationTimeResponseDto> findAll() {
        List<ReservationTime> reservationTimes = repository.findAll();
        return reservationTimes.stream()
                .map(this::convertToReservationTimeResponseDto)
                .toList();
    }

    public List<AvailableReservationTimeResponseDto> findReservationTimesWithAvailableStatus(Long themeId,
                                                                                             LocalDate date) {
        List<ReservationTime> allTime = repository.findAll();
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마입니다."));
        Set<ReservationTime> reservationTimesByThemeAndDate = reservationRepository.findByThemeAndDate(theme, date)
                .stream()
                .map(Reservation::getReservationTime)
                .collect(Collectors.toSet());

        return allTime.stream()
                .map(reservationTime ->
                        AvailableReservationTimeResponseDto.from(
                                reservationTime,
                                reservationTimesByThemeAndDate.contains(reservationTime)
                        )
                )
                .toList();
    }

    public void delete(Long id) {
        ReservationTime reservationTime = repository.findByIdOrThrow(id);
        if (reservationRepository.existsByReservationTime(reservationTime)) {
            throw new AlreadyReservedTimeException("예약에서 사용 중인 시간입니다.");
        }
        repository.delete(id);
    }

    public ReservationTimeResponseDto add(ReservationTimeRequestDto requestDto) {
        ReservationTime reservationTime = convertToReservationTimeRequestDto(requestDto);
        validateDuplicateTime(reservationTime);
        ReservationTime savedReservationTime = repository.add(reservationTime);
        return convertToReservationTimeResponseDto(savedReservationTime);
    }

    private void validateDuplicateTime(ReservationTime inputReservationTime) {
        boolean exists = repository.existsByReservationTime(inputReservationTime.getStartAt());

        if (exists) {
            throw new DuplicateReservationException("이미 등록되어 있는 예약 시간입니다.");
        }
    }

    private ReservationTime convertToReservationTimeRequestDto(ReservationTimeRequestDto requestDto) {
        return requestDto.toEntity();
    }

    private ReservationTimeResponseDto convertToReservationTimeResponseDto(ReservationTime reservationTime) {
        return ReservationTimeResponseDto.of(reservationTime);
    }
}
