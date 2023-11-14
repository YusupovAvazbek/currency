package fido.currency.service.Impl;

import fido.currency.dto.ApiResult;
import fido.currency.dto.CurrencyDto;
import fido.currency.dto.UserDto;
import fido.currency.model.Currency;
import fido.currency.repository.CurrencyRepository;
import fido.currency.service.CurrencyService;
import fido.currency.service.additional.AppStatusMessages;
import fido.currency.service.mapper.CurrencyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static fido.currency.service.additional.AppStatusCodes.*;
import static javax.security.auth.callback.ConfirmationCallback.OK;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    public static final String DATE_SORT_PROPERTY = "Ccy";
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    @Override
    public ApiResult<List<CurrencyDto>> getAllCurrency(int page, int size) {
        try {
            page = Integer.max(page, 0);
            size = Integer.max(size, 0);

            Pageable pageable = PageRequest.of(page, size, Sort.by(DATE_SORT_PROPERTY).descending());

            List<Currency> all = currencyRepository.findAllByOrderByDateDesc(pageable);
            return ApiResult.<List<CurrencyDto>>builder()
                    .data(all.stream().map(cur -> currencyMapper.toDto(cur)).toList())
                    .build();
        }catch (Exception e){
            return ApiResult.<List<CurrencyDto>>builder()
                    .code(DATABASE_ERROR_CODE)
                    .message("Error while saving user: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ApiResult<CurrencyDto> getCurrencyById(Long id) {
        if(id == null){
            return ApiResult.<CurrencyDto>builder()
                    .message(AppStatusMessages.NULL_VALUE)
                    .code(OK)
                    .build();
        }
        Optional<Currency> byId = currencyRepository.findById(id);
        if(byId.isEmpty()){
            return ApiResult.<CurrencyDto>builder()
                    .code(NOT_FOUND_ERROR_CODE)
                    .message(AppStatusMessages.NOT_FOUND)
                    .build();
        }

        return ApiResult.<CurrencyDto>builder()
                .code(OK_CODE)
                .message(AppStatusMessages.OK)
                .data(currencyMapper.toDto(byId.get()))
                .build();
    }
}
