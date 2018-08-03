package cf.nirvandil.fhdomainadder.web.converters;

import cf.nirvandil.fhdomainadder.model.panel.DomainCreationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainCreationRequestToStringConverter implements Converter<String, DomainCreationRequest> {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public DomainCreationRequest convert(String source) {
        log.debug("{}", source);
        return objectMapper.readValue(source, DomainCreationRequest.class);
    }
}
