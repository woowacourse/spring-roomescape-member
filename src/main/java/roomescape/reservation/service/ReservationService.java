package roomescape.reservation.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.common.globalexception.BadRequestException;
import roomescape.common.globalexception.ConflictException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.ReservationMapper;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationFilterCondition;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.domain.dto.ReservationResDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.ReservationTimeMapper;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.dto.ReservationTimeResDto;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeResDto;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
        ReservationRepository repository,
        ReservationTimeRepository reservationTimeRepository,
        ThemeRepository themeRepository,
        MemberRepository memberRepository
    ) {
        this.repository = repository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResDto> findAll(ReservationFilterCondition filterCondition) {
        List<Reservation> reservations = repository.findAll()
            .stream()
            .filter(reservation -> satisfiesFilterCondition(reservation, filterCondition))
            .toList();
        return reservations.stream()
            .map(this::convertReservationResDto)
            .collect(Collectors.toList());
    }

    private boolean satisfiesFilterCondition(Reservation reservation, ReservationFilterCondition filterCondition) {
        if (filterCondition.getThemeId().isPresent()) {
            Theme theme = themeRepository.findById(filterCondition.getThemeId().get())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 테마입니다."));
            if (!reservation.getTheme().equals(theme)) {
                return false;
            }
        }
        if (filterCondition.getMemberId().isPresent()) {
            Member member = memberRepository.findById(filterCondition.getMemberId().get())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 회원입니다."));
            if (!reservation.getName().equals(member.getName())) {
                return false;
            }
        }

        if (filterCondition.getDateFrom().isPresent()) {
            if (!reservation.getDate().isAfter(filterCondition.getDateFrom().get())) {
                return false;
            }
        }

        if (filterCondition.getDateTo().isPresent()) {
            return reservation.getDate().isBefore(filterCondition.getDateTo().get());
        }

        return true;
    }

    public ReservationResDto add(Member member, ReservationReqDto reqDto) {
        Reservation reservation = convertReservation(member, reqDto);
        validateDuplicateDateTime(reservation);
        Reservation savedReservation = repository.add(reservation);
        return convertReservationResDto(savedReservation);
    }

    public void delete(Long id) {
        repository.findByIdOrThrow(id);
        repository.delete(id);
    }

    private void validateDuplicateDateTime(Reservation inputReservation) {
        List<Reservation> reservations = repository.findAll();
        reservations.stream()
            .filter(inputReservation::isSameDateTime)
            .findAny()
            .ifPresent((reservation) -> {
                throw new ConflictException("이미 예약되어 있는 시간입니다.");
            });
    }

    private Reservation convertReservation(Member member, ReservationReqDto dto) {
        return ReservationMapper.toEntity(
            member,
            dto,
            findExistingReservationTimeById(dto.timeId()),
            findExistingThemeById(dto.themeId())
        );
    }

    private ReservationResDto convertReservationResDto(Reservation reservation) {
        Member member = memberRepository.findByName(reservation.getName())
            .orElseThrow(() -> new BadRequestException("존재하지 않는 회원입니다."));
        ReservationTimeResDto reservationTimeResDto = ReservationTimeMapper.toResDto(reservation.getReservationTime());
        ThemeResDto themeResDto = ThemeResDto.from(reservation.getTheme());
        return ReservationMapper.toResDto(reservation, member, reservationTimeResDto, themeResDto);
    }

    private ReservationTime findExistingReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("존재하지 않는 예약 시간입니다."));
    }

    private Theme findExistingThemeById(Long id) {
        return themeRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("존재하지 않는 테마입니다."));
    }
}
