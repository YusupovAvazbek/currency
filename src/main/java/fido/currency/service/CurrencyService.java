package fido.currency.service;

import fido.currency.dto.ApiResult;
import fido.currency.dto.CurrencyDto;
import java.util.List;

public interface CurrencyService {
    ApiResult<List<CurrencyDto>> getAllCurrency(int page, int size);
    ApiResult<CurrencyDto> getCurrencyById(Long id);
}
