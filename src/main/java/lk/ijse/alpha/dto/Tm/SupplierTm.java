package lk.ijse.alpha.dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SupplierTm {
    private String supplierId;
    private String supplierName;
    private String supplierContact;
    private String supplierAddress;
}
