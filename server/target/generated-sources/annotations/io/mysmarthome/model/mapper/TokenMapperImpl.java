package io.mysmarthome.model.mapper;

import io.mysmarthome.model.dto.TokenDto;
import io.mysmarthome.model.entity.Recipient;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-07-01T05:07:48+0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Ubuntu)"
)
@Component
public class TokenMapperImpl implements TokenMapper {

    @Override
    public Recipient toRecipient(TokenDto dto) {
        if ( dto == null ) {
            return null;
        }

        Recipient recipient = new Recipient();

        return recipient;
    }

    @Override
    public TokenDto toDto(Recipient recipient) {
        if ( recipient == null ) {
            return null;
        }

        TokenDto tokenDto = new TokenDto();

        tokenDto.setToken( recipient.getAddress() );

        return tokenDto;
    }
}
