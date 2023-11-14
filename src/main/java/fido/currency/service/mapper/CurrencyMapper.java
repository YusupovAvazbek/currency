package fido.currency.service.mapper;

import fido.currency.dto.CurrencyDto;
import fido.currency.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    @Mapping(target = "date", expression = "java(mapDate(currencyDto.getDate()))")
    Currency toEntity(CurrencyDto currencyDto);

    CurrencyDto toDto(Currency currency);

    default LocalDate mapDate(String date) {
        return date != null ? LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")) : null;
    }
}
