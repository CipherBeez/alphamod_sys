package lk.ijse.alpha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ItemDto {
    private String itemId;
    private String itemName;
    private int quantity;
    private double buyPrice;
    private double sellPrice;
}

