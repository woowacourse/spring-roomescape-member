package roomescape.domain;

import java.time.LocalDate;
import roomescape.domain.exception.InvalidDomainException;
import roomescape.domain.policy.ReservationPolicy;

public class Reservation {
    private static final int MAX_NAME_LENGTH = 30;

    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    // 새 예약 생성용 - 정책 검증 포함
    // TODO 정적 팩토리 메서드 적용 및 ~Entity 영속성 DB 매핑 엔티티 제거
    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme, ReservationPolicy policy) {
        validate(name, date, time, theme);
        policy.validateCreatable(date, time.getStartAt());
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    // DB 재구성용 - 불변식 검증만
    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private static void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDomainException("예약자 이름은 비어 있을 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidDomainException(
                    "예약자 이름은 " + MAX_NAME_LENGTH + "자를 초과할 수 없습니다."
            );
        }
    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidDomainException("예약 날짜는 비어 있을 수 없습니다.");
        }
    }

    private static void validateTime(ReservationTime time) {
        if (time == null) {
            throw new InvalidDomainException("예약 시간은 비어 있을 수 없습니다.");
        }
    }

    private static void validateTheme(Theme theme) {
        if (theme == null) {
            throw new InvalidDomainException("예약 테마는 비어 있을 수 없습니다.");
        }
    }


    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
