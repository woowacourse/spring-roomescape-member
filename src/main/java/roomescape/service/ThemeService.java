package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.Theme;
import roomescape.global.auth.Accessor;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ThemeRegisterCommand;
import roomescape.service.result.ThemeRegisterResult;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeRegisterResult register(Accessor accessor, ThemeRegisterCommand command) {
        accessor.validateAdmin();
        validateDuplicationName(command);

        Theme theme = new Theme(command.name(), command.description(), command.thumbnailImageUrl());

        return ThemeRegisterResult.from(themeRepository.save(theme));
    }


    public void remove(Accessor accessor, long id) {
        accessor.validateAdmin();
        themeRepository.findById(id)
                .ifPresent(existingTheme -> {
                        existingTheme.deactivate();
                        themeRepository.update(existingTheme);
                    });
    }

    private void validateDuplicationName(ThemeRegisterCommand command) {
        if (themeRepository.existByNameAndIsActiveFalse(command.name())) {
            throw new DuplicateEntityException("이미 존재하는 테마입니다. 테마 명: %s", command.name());
        }
    }
}
