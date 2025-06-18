package lk.ijse.alpha.model;

import lk.ijse.alpha.dto.ItemDto;
import lk.ijse.alpha.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemModel {

        public boolean saveItem(ItemDto itemDto) throws SQLException {
            return CrudUtil.execute(
                    "INSERT INTO Item VALUES(?,?,?,?,?,?)",
                    itemDto.getItemId(),
                    itemDto.getItemName(),
                    itemDto.getQuantity(),
                    itemDto.getBuyPrice(),
                    itemDto.getSellPrice(),
                    itemDto.getQuantity() * itemDto.getBuyPrice()
            );
        }

        public boolean updateItem(ItemDto itemDto) throws SQLException {
            return CrudUtil.execute(
                    "UPDATE Item SET item_name = ? , quantity = ? , buying_price = ? , selling_price = ?, total = ? WHERE item_id = ?",
                    itemDto.getItemName(),
                    itemDto.getQuantity(),
                    itemDto.getBuyPrice(),
                    itemDto.getSellPrice(),
                    itemDto.getQuantity() * itemDto.getBuyPrice(),
                    itemDto.getItemId()
            );
        }

        public boolean deleteItem(String itemId) throws SQLException, ClassNotFoundException {
            return CrudUtil.execute("DELETE FROM item WHERE item_id = ? ",
                    itemId);
        }

        public ArrayList<ItemDto> getAllItem() throws SQLException, ClassNotFoundException {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM item");
            ArrayList<ItemDto> itemDtoArrayList = new ArrayList<>();
            while (resultSet.next()){
                ItemDto itemDto = new ItemDto(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5),
                        resultSet.getDouble(6)
                );
                itemDtoArrayList.add(itemDto);
            }
            return itemDtoArrayList;

        }
  /*  public String getNextItemId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT item_id FROM item ORDER BY item_id DESC LIMIT 1");
        char tableChartacter = 'I';

        if(resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChartacter + "%03d" , nextIdNumber);

            return nextIdString;
        }
        return tableChartacter + "001";
    }*/

    public String getNextItemId() throws SQLException, ClassNotFoundException {
        // Only get IDs that match the expected pattern
        ResultSet resultSet = CrudUtil.execute("SELECT item_id FROM item WHERE item_id REGEXP '^I[0-9]+$' ORDER BY item_id DESC LIMIT 1");
        char tableCharacter = 'I';

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
        public ArrayList<ItemDto> searchItem(String searchText) throws SQLException {
            ArrayList<ItemDto> dtos = new ArrayList<>();
            String sql = "SELECT * FROM item WHERE item_id LIKE ? OR item_name LIKE ? OR quantity LIKE ? OR buying_price LIKE ? OR selling_price LIKE ?";
            String pattern = "%" + searchText + "%";
            ResultSet resultSet = CrudUtil.execute(sql , pattern , pattern , pattern , pattern , pattern);

            while (resultSet.next()){
                ItemDto itemDto = new ItemDto(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5),
                        resultSet.getDouble(6)
                );
                dtos.add(itemDto);
            }
            return dtos;
        }

       /* public boolean reduceQty(CartDto cartDto) throws SQLException {
            return CrudUtil.execute("UPDATE item SET quantity = quantity - ? WHERE item_id = ?",
                    cartDto.getQuantity(),
                    cartDto.getItemId()
            );
        }*/
        public ArrayList<String> getItemNames() throws SQLException {
            ResultSet resultSet = CrudUtil.execute("SELECT item_name FROM item");
            ArrayList<String> itemNames = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                itemNames.add(name);
            }
            return itemNames;
        }
        public ItemDto findIdByName(String name) throws SQLException {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM item WHERE item_name = ?",
                    name
            );
            if (resultSet.next()) {
                return new ItemDto(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5),
                        resultSet.getDouble(6)
                );
            }
            return null;
        }
       /* public boolean reduceQtyNew(SupOrderCartDto supOrderCartDto) throws SQLException {
            return CrudUtil.execute("UPDATE item SET quantity = quantity - ? WHERE item_id = ?",
                    supOrderCartDto.getQuantity(),
                    supOrderCartDto.getItemId()
            );
        }
*/

        public boolean updateItemQty(String itemId, int cartQty) {
            try {
                return CrudUtil.execute("UPDATE item SET quantity = quantity + ? WHERE item_id = ?",
                        cartQty,
                        itemId
                );
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
}
