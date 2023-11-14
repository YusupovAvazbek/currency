package fido.currency.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> {
    private T data;
    private String message;
    int code;
}
