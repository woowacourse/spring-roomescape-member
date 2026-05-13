package roomescape.theme.exception;

import roomescape.common.exception.ResourceInUseException;

public class ThemeResourceInUseException extends ResourceInUseException {

    public ThemeResourceInUseException(Long id) {
        super("예약이 존재하는 테마는 삭제할 수 없습니다. id=" + id);
    }
}
