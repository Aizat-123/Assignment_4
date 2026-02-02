package repository;

import model.*;
import repository.interfaces.WasteRepository;
import utils.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WasteItemsRepository implements WasteRepository {

    private final RecyclingCenterRepository centerRepository = new RecyclingCenterRepository();

    @Override
    public void create(WasteItems entity) {
        String sql = """
                INSERT INTO waste_items (id, name, waste_type, weight, recyclable, center_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getWasteType());
            ps.setDouble(4, entity.getWeight());
            ps.setBoolean(5, entity.isRecyclable());
            ps.setInt(6, entity.getCenterId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseOperationException("Failed to create waste item");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating waste item: " + e.getMessage());
        }
    }

    @Override
    public Optional<WasteItems> findById(Integer id) {
        String sql = """
                SELECT w.*, c.center_name, c.capacity
                FROM waste_items w
                LEFT JOIN recycling_center c ON w.center_id = c.center_id
                WHERE w.id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                WasteItems item = mapResultSetToWasteItem(rs);
                int centerId = rs.getInt("center_id");
                if (!rs.wasNull()) {
                    Optional<RecyclingCenter> center = centerRepository.findById(centerId);
                    setCenterFieldViaReflection(item, center.orElse(null));
                }

                return Optional.of(item);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding waste item: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseOperationException("Error processing waste item: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public WasteItems getById(Integer id) {
        return findById(id).orElse(null);
    }

    @Override
    public Optional<WasteItems> findByIdOptional(Integer id) {
        return findById(id);
    }

    @Override
    public List<WasteItems> findAll() {
        List<WasteItems> items = new ArrayList<>();
        String sql = """
                SELECT w.*, c.center_name, c.capacity
                FROM waste_items w
                LEFT JOIN recycling_center c ON w.center_id = c.center_id
                ORDER BY w.id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                WasteItems item = mapResultSetToWasteItem(rs);
                int centerId = rs.getInt("center_id");
                if (!rs.wasNull()) {
                    Optional<RecyclingCenter> center = centerRepository.findById(centerId);
                    setCenterFieldViaReflection(item, center.orElse(null));
                }

                items.add(item);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error loading waste items: " + e.getMessage());
        } catch (Exception e) {
            throw new DatabaseOperationException("Error processing waste items: " + e.getMessage());
        }

        return items;
    }

    @Override
    public void update(WasteItems entity) {
        String sql = """
                UPDATE waste_items
                SET name = ?, waste_type = ?, weight = ?, recyclable = ?, center_id = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getWasteType());
            ps.setDouble(3, entity.getWeight());
            ps.setBoolean(4, entity.isRecyclable());
            ps.setInt(5, entity.getCenterId());
            ps.setInt(6, entity.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Waste item with id " + entity.getId() + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating waste item: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM waste_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting waste item: " + e.getMessage());
        }
    }

    @Override
    public double getTotalWeightByCenter(int centerId) {
        String sql = "SELECT COALESCE(SUM(weight), 0) as total_weight FROM waste_items WHERE center_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, centerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_weight");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error calculating center weight: " + e.getMessage());
        }

        return 0.0;
    }

    @Override
    public List<WasteItems> findByCriteria(Criteria<WasteItems> criteria) {
        List<WasteItems> allItems = findAll();
        List<WasteItems> result = new ArrayList<>();
        for (WasteItems item : allItems) {
            if (criteria.test(item)) {
                result.add(item);
            }
        }

        return result;
    }

    public List<WasteItems> findRecyclableItems() {
        return findByCriteria(item -> item.isRecyclable());
    }

    public List<WasteItems> findByWasteType(String wasteType) {
        return findByCriteria(item -> item.getWasteType().equalsIgnoreCase(wasteType));
    }

    public List<WasteItems> findByCenter(int centerId) {
        return findByCriteria(item -> item.getCenterId() == centerId);
    }

    private WasteItems mapResultSetToWasteItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String wasteType = rs.getString("waste_type");
        double weight = rs.getDouble("weight");
        boolean recyclable = rs.getBoolean("recyclable");
        int centerId = rs.getInt("center_id");
        return switch (wasteType.toLowerCase()) {
            case "plastic" -> new PlasticWaste(id, name, weight, recyclable);
            case "electronic" -> new ElectronicWaste(id, name, weight, recyclable);
            case "paper" -> new PaperWaste(id, name, weight, recyclable);
            default -> throw new IllegalArgumentException("Unknown waste type: " + wasteType);
        };
    }

    private void setCenterFieldViaReflection(WasteItems item, RecyclingCenter center) {
        try {
            java.lang.reflect.Field centerField = WasteItems.class.getDeclaredField("center");
            centerField.setAccessible(true);
            centerField.set(item, center);
        } catch (NoSuchFieldException e) {
            if (center != null) {
                item.setCenterId(center.getId());
            }
        } catch (IllegalAccessException e) {
            throw new DatabaseOperationException("Error setting center via reflection: " + e.getMessage());
        }
    }

    public void createBatch(List<WasteItems> items) {
        String sql = """
                INSERT INTO waste_items (id, name, waste_type, weight, recyclable, center_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (WasteItems item : items) {
                    ps.setInt(1, item.getId());
                    ps.setString(2, item.getName());
                    ps.setString(3, item.getWasteType());
                    ps.setDouble(4, item.getWeight());
                    ps.setBoolean(5, item.isRecyclable());
                    ps.setInt(6, item.getCenterId());
                    ps.addBatch();
                }

                int[] results = ps.executeBatch();
                conn.commit();

                System.out.println("Batch insert completed. " + results.length + " items inserted.");

            } catch (SQLException e) {
                if (conn != null) {
                    conn.rollback();
                }
                throw new DatabaseOperationException("Batch insert failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database connection error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}