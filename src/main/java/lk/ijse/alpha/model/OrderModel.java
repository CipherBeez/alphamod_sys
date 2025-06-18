package lk.ijse.alpha.model;

import lk.ijse.alpha.dto.OrderDto;
import lk.ijse.alpha.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModel {
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

public String getNextOrderId() throws SQLException, ClassNotFoundException {
    // Only get IDs that match the expected pattern
    ResultSet resultSet = CrudUtil.execute("SELECT order_id FROM orders WHERE order_id REGEXP '^I[0-9]+$' ORDER BY order_id DESC LIMIT 1");
    char tableCharacter = 'O';

    if(resultSet.next()){
        String lastId = resultSet.getString(1);
        String lastIdNumberString = lastId.substring(1);
        int lastIdNumber = Integer.parseInt(lastIdNumberString);
        int nextIdNumber = lastIdNumber + 1;
        String nextIdString = tableCharacter + String.format("%03d", nextIdNumber);
        return nextIdString;
    }
    return tableCharacter + "001";
}
}
