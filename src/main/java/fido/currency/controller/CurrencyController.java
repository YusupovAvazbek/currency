package fido.currency.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fido.currency.dto.ApiResult;
import fido.currency.dto.CurrencyDto;
import fido.currency.service.CurrencyService;
import fido.currency.service.Impl.ExchangeRateSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;
    @GetMapping("/currencies")
    public ApiResult<List<CurrencyDto>> getAllCurrency(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        return currencyService.getAllCurrency(page,size);
    }
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/currency/{id}")
    public ApiResult<CurrencyDto> getCurrencyById(@PathVariable Long id){
        return currencyService.getCurrencyById(id);
    }

}
