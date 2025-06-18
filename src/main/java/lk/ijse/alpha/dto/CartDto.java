package lk.ijse.alpha.dto;

import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CartDto {
    private String orderId;
    private String itemId;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private Button btnRemove;
}
