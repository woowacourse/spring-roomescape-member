package roomescape.reservationTime.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.globalException.CustomException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.ReservationTimeMapper;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.AvailableReservationTimeResDto;
import roomescape.reservationTime.domain.dto.ReservationTimeReqDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
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

    public ReservationTimeResDto add(ReservationTimeReqDto dto) {
        ReservationTime reservationTime = convertToReservationTimeReqDto(dto);
        validateDuplicateTime(reservationTime);
        ReservationTime savedReservationTime = repository.add(reservationTime);
        return convertToReservationTimeResDto(savedReservationTime);
    }

    public List<ReservationTimeResDto> readAll() {
        List<ReservationTime> reservationTimes = repository.findAll();
        return reservationTimes.stream()
            .map(this::convertToReservationTimeResDto)
            .toList();
    }

    public List<AvailableReservationTimeResDto> readAllAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> allTime = repository.findAll();
        Theme theme = themeRepository.findByIdOrThrow(themeId);
        Set<ReservationTime> reservationTimesByThemeAndDate = reservationRepository.findByThemeAndDate(theme, date)
            .stream()
            .map(Reservation::getReservationTime)
            .collect(Collectors.toSet());

        return allTime.stream()
            .map(reservationTime ->
                AvailableReservationTimeResDto.from(
                    reservationTime,
                    reservationTimesByThemeAndDate.contains(reservationTime)
                )
            )
            .toList();
    }

    public void delete(Long id) {
        ReservationTime reservationTime = repository.findByIdOrThrow(id);
        if (reservationRepository.existsByReservationTime(reservationTime)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "예약에서 사용 중인 시간입니다.");
        }
        repository.delete(id);
    }

    private void validateDuplicateTime(ReservationTime inputReservationTime) {
        List<ReservationTime> reservationTimes = repository.findAll();
        for (ReservationTime reservationTime : reservationTimes) {
            if (inputReservationTime.isSameTime(reservationTime)) {
                throw new CustomException(HttpStatus.CONFLICT, "이미 등록되어 있는 시간입니다.");
            }
        }
    }

    private ReservationTime convertToReservationTimeReqDto(ReservationTimeReqDto dto) {
        return ReservationTimeMapper.toEntity(dto);
    }

    public ReservationTimeResDto convertToReservationTimeResDto(ReservationTime reservationTime) {
        return ReservationTimeMapper.toResDto(reservationTime);
    }
}
