package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.dto.ReservationUpdateDtoDateAndTimeIdOnly;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.PastDateBookingException;
import roomescape.exception.PastDateCancellationException;
import roomescape.exception.PastDateModificationException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponseDTO> readAllReservation() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponseDTO::from)
                .toList();
    }

    public List<ReservationResponseDTO> findAllByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 비어있거나 공백일 수 없습니다.");
        }
        return reservationRepository.findAllByUsername(username)
                .stream()
                .map(ReservationResponseDTO::from)
                .toList();
    }

    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("ID로 예약 조회 실패: " + id));
        return ReservationResponseDTO.from(reservation);
    }

    public ReservationResponseDTO addReservation(ReservationRequestDTO reservationRequestDTO) {
        ReservationTime time = reservationTimeRepository.findById(reservationRequestDTO.timeId())
                .orElseThrow(() ->
                        new ReservationTimeNotFoundException("ID로 예약 시간 조회 실패: " + reservationRequestDTO.timeId())
                );
        Theme theme = themeRepository.findById(reservationRequestDTO.themeId())
                .orElseThrow(() ->
                        new ThemeNotFoundException("ID로 테마 조회 실패: " + reservationRequestDTO.themeId())
                );

        validateBookingDate(LocalDateTime.of(reservationRequestDTO.date(), time.getStartAt()));
        validateNotDuplicated(reservationRequestDTO.date(), time, theme);

        Reservation reservation = Reservation.withoutId(
                reservationRequestDTO.name(),
                reservationRequestDTO.date(),
                time,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.from(savedReservation);
    }

    public int update(Long reservationId, ReservationUpdateDtoDateAndTimeIdOnly updateDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException("수정 대상을 찾을 수 없음. ID: " + reservationId)
                );
        Theme theme = themeRepository.findById(reservation.getThemeId())
                .orElseThrow(() ->
                        new ThemeNotFoundException("테마를 찾을 수 없음. ID: " + reservation.getThemeId())
                );
        ReservationTime reservationTimeForUpdate = reservationTimeRepository.findById(updateDto.timeId())
                .orElseThrow(() ->
                        new ReservationTimeNotFoundException("수정할 예약 시간을 찾을 수 없음. ID: " + updateDto.timeId())
                );

        // 현재 예약이 이미 지난 것인지 체크 (수정 불가 사유)
        validateModificationDate(LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt()));
        // 새로 변경하려는 시간이 과거인지 체크 (예약 불가 사유)
        validateBookingDate(LocalDateTime.of(updateDto.date(), reservationTimeForUpdate.getStartAt()));

        validateNotDuplicated(updateDto.date(), reservationTimeForUpdate, theme);

        reservation.changeDateAndTime(updateDto.date(), reservationTimeForUpdate);

        return reservationRepository.update(reservation);
    }

    public int deleteReservationByUsernameAndDateAndTimeIdAndThemeId(ReservationRequestDTO requestDTO) {
        ReservationTime time = reservationTimeRepository.findById(requestDTO.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException("취소 대상을 위한 시간 조회 실패: " + requestDTO.timeId()));

        validateCancellationDate(LocalDateTime.of(requestDTO.date(), time.getStartAt()));

        int deletedRows = reservationRepository.deleteReservationWith(
                requestDTO.name(),
                requestDTO.date(),
                requestDTO.timeId(),
                requestDTO.themeId()
        );
        if (deletedRows == 0) {
            throw new ReservationNotFoundException("취소할 예약 정보를 찾을 수 없음: " + requestDTO.name());
        }

        return deletedRows;
    }

    private void validateBookingDate(LocalDateTime targetDateTime) {
        if (targetDateTime.isBefore(LocalDateTime.now())) {
            throw new PastDateBookingException("과거 시간으로 예약 시도: " + targetDateTime);
        }
    }

    private void validateModificationDate(LocalDateTime targetDateTime) {
        if (targetDateTime.isBefore(LocalDateTime.now())) {
            throw new PastDateModificationException("이미 지난 예약 수정 시도: " + targetDateTime);
        }
    }

    private void validateCancellationDate(LocalDateTime targetDateTime) {
        if (targetDateTime.isBefore(LocalDateTime.now())) {
            throw new PastDateCancellationException("이미 지난 예약 취소 시도: " + targetDateTime);
        }
    }

    private void validateNotDuplicated(LocalDate date, ReservationTime time, Theme theme) {
        if (reservationRepository.existsReservationWith(
                date,
                time.getId(),
                theme.getId()
        )) {
            throw new DuplicatedReservationException(date, time.getStartAt(), theme.getName());
        }
    }
}
