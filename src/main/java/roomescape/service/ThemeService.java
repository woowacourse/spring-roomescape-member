package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.Theme;
import roomescape.global.auth.Accessor;
import roomescape.global.auth.ForbiddenException;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ThemeRegisterCommand;
import roomescape.service.result.ThemeRegisterResult;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeRegisterResult register(Accessor accessor, ThemeRegisterCommand command) {
        if(!accessor.isAdmin()){
            throw new ForbiddenException("테마 추가는 관리자만 가능합니다.");
        }

        if(themeRepository.existByName(command.name())){
            throw new DuplicateEntityException("이미 존재하는 테마입니다. 테마 명: %s", command.name());
        }

        Theme theme = new Theme(command.name(), command.description(), command.thumbnailImageUrl());

        return ThemeRegisterResult.from(themeRepository.save(theme));
    }
}
