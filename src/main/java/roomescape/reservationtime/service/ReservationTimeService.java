package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.common.globalexception.BadRequestException;
import roomescape.common.globalexception.ConflictException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.ReservationTimeMapper;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.dto.ReservationTimeReqDto;
import roomescape.reservationtime.domain.dto.ReservationTimeResDto;
import roomescape.reservationtime.domain.dto.ReservationTimeStatusResDto;
import roomescape.reservationtime.repository.ReservationTimeRepository;
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

    public List<ReservationTimeResDto> findAll() {
        List<ReservationTime> reservationTimes = repository.findAll();
        return reservationTimes.stream()
            .map(this::convertToReservationTimeResDto)
            .toList();
    }

    public List<ReservationTimeStatusResDto> findAllAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> allTime = repository.findAll();
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new BadRequestException("존재하지 않는 테마입니다."));
        Set<ReservationTime> reservationTimesByThemeAndDate = reservationRepository.findByThemeAndDate(theme, date)
            .stream()
            .map(Reservation::getReservationTime)
            .collect(Collectors.toSet());

        return allTime.stream()
            .map(reservationTime ->
                ReservationTimeStatusResDto.from(
                    reservationTime,
                    reservationTimesByThemeAndDate.contains(reservationTime)
                )
            )
            .toList();
    }

    public void delete(Long id) {
        ReservationTime reservationTime = repository.findByIdOrThrow(id);
        if (reservationRepository.existsByReservationTime(reservationTime)) {
            throw new BadRequestException("예약에서 사용 중인 시간입니다.");
        }
        repository.delete(id);
    }

    public ReservationTimeResDto add(ReservationTimeReqDto reqDto) {
        ReservationTime reservationTime = convertToReservationTimeReqDto(reqDto);
        validateDuplicateTime(reservationTime);
        ReservationTime savedReservationTime = repository.add(reservationTime);
        return convertToReservationTimeResDto(savedReservationTime);
    }

    private void validateDuplicateTime(ReservationTime inputReservationTime) {
        List<ReservationTime> reservationTimes = repository.findAll();
        for (ReservationTime reservationTime : reservationTimes) {
            if (inputReservationTime.isSameTime(reservationTime)) {
                throw new ConflictException("이미 등록되어 있는 시간입니다.");
            }
        }
    }

    private ReservationTime convertToReservationTimeReqDto(ReservationTimeReqDto reqDto) {
        return ReservationTimeMapper.toEntity(reqDto);
    }

    public ReservationTimeResDto convertToReservationTimeResDto(ReservationTime reservationTime) {
        return ReservationTimeMapper.toResDto(reservationTime);
    }
}
