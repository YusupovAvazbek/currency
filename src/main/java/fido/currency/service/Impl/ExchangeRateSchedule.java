package fido.currency.service.Impl;

import fido.currency.dto.CurrencyDto;
import fido.currency.repository.CurrencyRepository;
import fido.currency.service.additional.AppStatusMessages;
import fido.currency.service.exception.CbuNotAnswerException;
import fido.currency.service.mapper.CurrencyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateSchedule {
    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;
    private final CurrencyMapper currencyMapper;
    @Scheduled(cron = "0 0 * * *")
    public void writeRateCurrency(){
        try {
            ResponseEntity<List<CurrencyDto>> response = restTemplate.exchange(
                    "https://cbu.uz/uz/arkhiv-kursov-valyut/json/",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CurrencyDto>>() {
                    });
            if(response.getStatusCode() == HttpStatus.OK){
                List<CurrencyDto> currencyDtoList = response.getBody();
                currencyDtoList.stream()
                        .map(dto -> currencyMapper.toEntity(dto))
                        .forEach(entity -> currencyRepository.save(entity));

            }

        }catch (Exception e){
            throw new CbuNotAnswerException(AppStatusMessages.UNEXPECTED_ERROR);
        }
    }



}
