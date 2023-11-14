package fido.currency.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String currencyCode;
    @Column(nullable = false)
    private String Ccy;
    @Column(nullable = false)
    private String CcyNmUz;
    @Column(nullable = false)
    private String CcyNmEng;
    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private LocalDate date;

}
