package lk.ijse.alpha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SupplierDto {
    private String supplierId;
    private String supplierName;
    private String supplierContact;
    private String supplierAddress;
}
