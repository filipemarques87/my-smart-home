package io.mysmarthome.model.mapper;

import io.mysmarthome.model.dto.TokenDto;
import io.mysmarthome.model.entity.Recipient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    @Mapping(target = "id", ignore = true)
    Recipient toRecipient(TokenDto dto);

    @Mapping(source = "address", target = "token")
    TokenDto toDto(Recipient recipient);
}
