package hu.webuni.spring.financialsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;


//it has to bi in the same packages as it is in the sender (@financialsystem)
@Data
@AllArgsConstructor
public class PaymentDto {
    long studentId;
    int cashPaidIn;
}
