package fido.currency.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto implements Serializable {
    private Long id;
    @JsonProperty("Code")
    private String currencyCode;
    @JsonProperty("Ccy")
    private String Ccy;
    @JsonProperty("CcyNm_UZ")
    private String CcyNmUz;
    @JsonProperty("CcyNm_EN")
    private String CcyNmEng;
    @JsonProperty("Rate")
    private Double rate;
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("Date")
    private String date;

}
