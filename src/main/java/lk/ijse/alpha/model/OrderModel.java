package lk.ijse.alpha.model;

import javafx.scene.control.Alert;
import lk.ijse.alpha.db.DBConnection;
import lk.ijse.alpha.dto.CartDto;
import lk.ijse.alpha.dto.OrderDto;
import lk.ijse.alpha.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModel {
    private ItemModel itemModel = new ItemModel();

    public boolean saveOrder(OrderDto orderDto) throws SQLException {
        return CrudUtil.execute("INSERT INTO orders (order_id, order_date) VALUES (?, ?)",
                orderDto.getOrderId(),
                orderDto.getOrderDate());

    }

    public boolean updateOrder(OrderDto orderDto) throws SQLException {
        return CrudUtil.execute("UPDATE orders SET order_date = ? WHERE order_id = ?",
                orderDto.getOrderDate(),
                orderDto.getOrderId());
    }

    public boolean deleteOrder(String orderId) throws SQLException {
        return CrudUtil.execute("DELETE FROM orders WHERE order_id = ?",
                orderId);
    }

    public ArrayList<OrderDto> getAllOrder() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders");
        ArrayList<OrderDto> orderDtos = new ArrayList<>();
        while (resultSet.next()) {
            OrderDto orderDto = new OrderDto(
                    resultSet.getString(1),
                    resultSet.getString(2)
            );
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }


    public String getNextOrderId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
        char tableCharacter = 'O';

        while (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = tableCharacter + String.format("%03d", nextIdNumber);
            return nextIdString;
        }
        return tableCharacter + "001";
    }

    public boolean placeOrder(OrderDto orderDto, ArrayList<CartDto> cartList) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = saveOrder(orderDto);
            if (!isOrderSaved) {
                connection.rollback();
                return false;
            }

            for (CartDto cartDto : cartList) {
                boolean isDetailSaved = CrudUtil.execute(
                        "INSERT INTO order_details (order_id, item_name, qty, total_price) VALUES (?, ?, ?, ?)",
                        orderDto.getOrderId(),
                        cartDto.getItemName(),
                        cartDto.getQuantity(),
                        cartDto.getUnitPrice() * cartDto.getQuantity()
                );

                if (!isDetailSaved) {
                    connection.rollback();
                    return false;
                }
                System.out.println("4");

                boolean isItemUpdated = itemModel.decreaseItemQty(cartDto.getItemId(), cartDto.getQuantity());
                if (!isItemUpdated) {
                    connection.rollback();
                    return false;
                }

            }

            connection.commit();
            return true;

        } catch (Exception e) {
            System.out.println("-1");
            connection.rollback();
            e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

}