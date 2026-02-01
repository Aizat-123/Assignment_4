package repository;

import model.*;
import repository.interfaces.CrudRepository;
import utils.DatabaseConnection;
import exception.DatabaseOperationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WasteItemsRepository implements CrudRepository<WasteItems, Integer> {

    public void create(WasteItems item) {
        String sql = """
                INSERT INTO waste_items (id, name, waste_type, weight, recyclable, center_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getWasteType());
            ps.setDouble(4, item.getWeight());
            ps.setBoolean(5, item.isRecyclable());
            ps.setInt(6, item.getCenterId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating waste item");
        }
    }

    public List<WasteItems> getAll() {
        List<WasteItems> list = new ArrayList<>();
        String sql = "SELECT * FROM waste_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error loading waste items");
        }
        return list;
    }

    public WasteItems getById(int id) {
        String sql = "SELECT * FROM waste_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding waste item");
        }
        return null;
    }

    public void delete(int id) {
        String sql = "DELETE FROM waste_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            if (ps.executeUpdate() == 0)
                throw new DatabaseOperationException("Waste item not found");

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting waste item");
        }
    }

    private WasteItems map(ResultSet rs) throws SQLException {
        return switch (rs.getString("waste_type")) {
            case "plastic" -> new PlasticWaste(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("weight"),
                    rs.getBoolean("recyclable"));
            case "electronic" -> new ElectronicWaste(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("weight"),
                    rs.getBoolean("recyclable"));
            default -> new PaperWaste(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("weight"),
                    rs.getBoolean("recyclable"));
        };
    }
    public double getTotalWeightByCenter(int centerId) {
        String sql = "SELECT SUM(weight) FROM waste_items WHERE center_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, centerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1); // если NULL → вернёт 0.0
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error calculating center weight");
        }

        return 0;
    }
}