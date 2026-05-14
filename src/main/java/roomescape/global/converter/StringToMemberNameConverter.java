package roomescape.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import roomescape.domain.vo.MemberName;

@Component
public class StringToMemberNameConverter implements Converter<String, MemberName> {
    @Override
    public MemberName convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }

        return new MemberName(source);
    }
}
