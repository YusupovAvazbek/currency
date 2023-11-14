package fido.currency.controller;
import fido.currency.dto.ApiResult;
import fido.currency.dto.CurrencyDto;
import fido.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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
