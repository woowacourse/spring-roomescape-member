package roomescape.business.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.Reservation;
import roomescape.business.ReservationTheme;
import roomescape.business.ReservationTime;
import roomescape.business.dto.ReservationRequestDto;
import roomescape.business.dto.ReservationResponseDto;
import roomescape.business.dto.ReservationThemeRequestDto;
import roomescape.business.dto.ReservationThemeResponseDto;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationThemeRepository reservationThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
    }

    public List<ReservationResponseDto> readReservationAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        new ReservationTimeResponseDto(
                                reservation.getTime().getId(),
                                reservation.getTime().getStartAt()
                        ),
                        new ReservationThemeResponseDto(
                                reservation.getTheme().getId(),
                                reservation.getTheme().getName(),
                                reservation.getTheme().getDescription(),
                                reservation.getTheme().getThumbnail()
                        )
                ))
                .toList();
    }

    public ReservationResponseDto readReservationOne(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                new ReservationTimeResponseDto(
                        reservation.getTime().getId(),
                        reservation.getTime().getStartAt()
                ),
                new ReservationThemeResponseDto(
                        reservation.getTheme().getId(),
                        reservation.getTheme().getName(),
                        reservation.getTheme().getDescription(),
                        reservation.getTheme().getThumbnail()
                )
        );
    }

    public Long createReservation(ReservationRequestDto reservationDto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationDto.timeId());
        ReservationTheme theme = reservationThemeRepository.findById(reservationDto.themeId());
        validatePastDateTime(reservationDto.date(), reservationTime.getStartAt());
        Reservation reservation = new Reservation(reservationDto.name(), reservationDto.date(), reservationTime, theme);
        validateDuplicatedReservation(reservation);
        return reservationRepository.add(reservation);
    }

    private void validatePastDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 일시로 예약을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByReservation(reservation)) {
            throw new IllegalArgumentException("이미 예약되었습니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public Long createTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        return reservationTimeRepository.add(new ReservationTime(reservationTimeRequestDto.startAt()));
    }

    public List<ReservationTimeResponseDto> readTimeAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(reservationTime -> new ReservationTimeResponseDto(
                        reservationTime.getId(),
                        reservationTime.getStartAt()
                ))
                .toList();
    }

    public ReservationTimeResponseDto readTimeOne(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id);
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    public void deleteTime(Long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new IllegalArgumentException("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(timeId);
    }

    public List<ReservationThemeResponseDto> readThemeAll() {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.findAll();
        return reservationThemes.stream()
                .map(reservationTheme -> new ReservationThemeResponseDto(
                                reservationTheme.getId(),
                                reservationTheme.getName(),
                                reservationTheme.getDescription(),
                                reservationTheme.getThumbnail()
                        )
                )
                .toList();
    }

    public ReservationThemeResponseDto createTheme(ReservationThemeRequestDto reservationThemeDto) {
        if (reservationThemeRepository.existByName(reservationThemeDto.name())) {
            throw new IllegalArgumentException("동일한 이름의 테마를 추가할 수 없습니다.");
        }
        Long id = reservationThemeRepository.add(new ReservationTheme(
                        reservationThemeDto.name(),
                        reservationThemeDto.description(),
                        reservationThemeDto.thumbnail()
                )
        );
        return new ReservationThemeResponseDto(
                id,
                reservationThemeDto.name(),
                reservationThemeDto.description(),
                reservationThemeDto.thumbnail()
        );
    }

    public void deleteTheme(Long id) {
        if (reservationRepository.existByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationThemeRepository.deleteById(id);
    }

    public List<ReservationThemeResponseDto> readBestReservedThemes() {
        LocalDate now = LocalDate.now();
        LocalDate start = calculateStartDate(now);
        LocalDate end = calculateEndDate(now);
        List<ReservationTheme> bestReservedReservationThemes = reservationThemeRepository.findByStartDateAndEndDateOrderByReservedDesc(
                start, end, 10);
        return bestReservedReservationThemes.stream()
                .map(bestTheme -> new ReservationThemeResponseDto(
                                bestTheme.getId(),
                                bestTheme.getName(),
                                bestTheme.getDescription(),
                                bestTheme.getThumbnail()
                        )
                )
                .toList();
    }

    private static LocalDate calculateEndDate(LocalDate nowDate) {
        return nowDate.minusDays(1);
    }

    private static LocalDate calculateStartDate(LocalDate nowDate) {
        return nowDate.minusDays(7);
    }
}
