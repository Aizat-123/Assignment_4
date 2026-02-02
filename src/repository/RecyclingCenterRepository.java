package repository;

import model.RecyclingCenter;
import model.Address;
import repository.interfaces.CrudRepository;
import utils.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecyclingCenterRepository implements CrudRepository<RecyclingCenter, Integer> {

    @Override
    public void create(RecyclingCenter entity) {
        String sql = """
                INSERT INTO recycling_center (center_id, center_name, capacity, address_id)
                VALUES (?, ?, ?, ?)
                """;

        // Сначала создаем Address если он есть
        if (entity.getAddress() != null) {
            createAddress(entity.getAddress());
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getCapacity());
            ps.setInt(4, entity.getAddress() != null ? 1 : null); // Упрощенная логика

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating recycling center: " + e.getMessage());
        }
    }

    @Override
    public Optional<RecyclingCenter> findById(Integer id) {
        String sql = """
                SELECT c.*, a.street, a.city, a.postal_code
                FROM recycling_center c
                LEFT JOIN address a ON c.address_id = a.address_id
                WHERE c.center_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Address address = null;
                if (rs.getInt("address_id") > 0) {
                    address = new Address(
                            rs.getString("street"),
                            rs.getString("city"),
                            rs.getString("postal_code")
                    );
                }
                RecyclingCenter center = new RecyclingCenter(
                        rs.getInt("center_id"),
                        rs.getString("center_name"),
                        rs.getInt("capacity"),
                        address
                );

                return Optional.of(center);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding recycling center: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public RecyclingCenter getById(Integer id){
        return findById(id).orElse(null);
    }

    @Override
    public List<RecyclingCenter> findAll() {
        List<RecyclingCenter> centers = new ArrayList<>();
        String sql = """
                SELECT c.*, a.street, a.city, a.postal_code
                FROM recycling_center c
                LEFT JOIN address a ON c.address_id = a.address_id
                ORDER BY c.center_id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Address address = null;
                if (rs.getInt("address_id") > 0) {
                    address = new Address(
                            rs.getString("street"),
                            rs.getString("city"),
                            rs.getString("postal_code")
                    );
                }

                RecyclingCenter center = new RecyclingCenter(
                        rs.getInt("center_id"),
                        rs.getString("center_name"),
                        rs.getInt("capacity"),
                        address
                );

                centers.add(center);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error loading recycling centers: " + e.getMessage());
        }

        return centers;
    }

    @Override
    public void update(RecyclingCenter entity) {
        String sql = """
                UPDATE recycling_center
                SET center_name = ?, capacity = ?, address_id = ?
                WHERE center_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCapacity());
            ps.setObject(3, entity.getAddress() != null ? 1 : null); // Упрощенная логика
            ps.setInt(4, entity.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResourceNotFoundException("Recycling center with id " + entity.getId() + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating recycling center: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM recycling_center WHERE center_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting recycling center: " + e.getMessage());
        }
    }

    public Optional<RecyclingCenter> findByIdOptional(Integer id) {
        return findById(id);
    }

    private void createAddress(Address address) {
        String sql = "INSERT INTO address (street, city, postal_code) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getPostalCode());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating address: " + e.getMessage());
        }
    }
}