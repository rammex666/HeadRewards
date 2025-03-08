package fr.rammex.headreward.database;


import fr.rammex.headreward.HeadReward;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DataManager {

    private static String dbName;
    private static File dbFile;

    public DataManager(String dbName, File dbFile) {
        DataManager.dbName = dbName;
        DataManager.dbFile = dbFile;
    }

    public void initialize() {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM head_data");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public static Connection getSQLConnection() {
        File folder = new File(dbFile, dbName + ".db");
        if (!folder.getParentFile().exists()) {
            folder.getParentFile().mkdirs();
        }
        if (!folder.exists()) {
            try {
                folder.createNewFile();
            } catch (IOException e) {
                HeadReward.instance.getLogger().log(Level.SEVERE, "File write error: " + dbName + ".db");
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + folder);
        } catch (SQLException | ClassNotFoundException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        }
        return null;
    }

    public void load() {
        try (Connection connection = getSQLConnection()) {
            Statement s = connection.createStatement();

            // Table for heads
            String headsData = "CREATE TABLE IF NOT EXISTS head_data (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`head_name` TEXT," +
                    "`head_id` TEXT," +
                    "`head_location` TEXT," +
                    "`head_reward` TEXT" +
                    ");";



            s.executeUpdate(headsData);

            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }

    public static void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ingored) {
        }
    }


    public static void addHead(String headName, String headId, String headLocation, String headReward) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO head_data (head_name, head_id, head_location, head_reward) VALUES(?,?,?,?)");
            ps.setString(1, headName);
            ps.setString(2, headId);
            ps.setString(3, headLocation);
            ps.setString(4, headReward);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while adding a head", ex);
        }
    }

    public static void removeHead(String headName) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM head_data WHERE head_name = ?");
            ps.setString(1, headName);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while removing a head", ex);
        }
    }

    public static void updateHead(String headId, String headName, String headReward) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE head_data SET head_id = ?, head_reward = ? WHERE head_name = ?");
            ps.setString(1, headId);
            ps.setString(2, headReward);
            ps.setString(3, headName);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while updating a head", ex);
        }
    }

    public static Map<String, List<String>> getHeads() {
        Map<String, List<String>> heads = new HashMap<>();
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM head_data");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String headId = rs.getString("head_name");
                List<String> headDetails = new ArrayList<>();
                headDetails.add(rs.getString("head_id"));
                headDetails.add(rs.getString("head_location"));
                headDetails.add(rs.getString("head_reward"));
                heads.put(headId, headDetails);
            }
            close(ps, rs);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while getting heads", ex);
        }
        return heads;
    }

    public static boolean headExists(String headName) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM head_data WHERE head_name = ?");
            ps.setString(1, headName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                close(ps, rs);
                return true;
            }
            close(ps, rs);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while checking if a head exists", ex);
        }
        return false;
    }

    public static boolean isHeadExist(String headId) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM head_data WHERE head_id = ?");
            ps.setString(1, headId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                close(ps, rs);
                return true;
            }
            close(ps, rs);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while checking if a head exists", ex);
        }
        return false;
    }

    public static String getHeadReward(String headId) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM head_data WHERE head_id = ?");
            ps.setString(1, headId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String headReward = rs.getString("head_reward");
                close(ps, rs);
                return headReward;
            }
            close(ps, rs);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while getting head reward", ex);
        }
        return null;
    }

    public static Location getHeadLocation(String headName) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM head_data WHERE head_name = ?");
            ps.setString(1, headName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String headLocation = rs.getString("head_location");
                close(ps, rs);
                Location location = stringToLocation(headLocation);
                return location;
            }
            close(ps, rs);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while getting head location", ex);
        }
        return null;
    }

    public static Location stringToLocation(String locString) {
        String[] parts = locString.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid location string format. Expected format: world,x,y,z");
        }

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) {
            throw new IllegalArgumentException("World not found: " + parts[0]);
        }

        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);

        return new Location(world, x, y, z);
    }

    public static String getHeadID(String headName) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM head_data WHERE head_name = ?");
            ps.setString(1, headName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String headId = rs.getString("head_id");
                close(ps, rs);
                return headId;
            }
            close(ps, rs);
        } catch (SQLException ex) {
            HeadReward.instance.getLogger().log(Level.SEVERE, "An error occurred while getting head ID", ex);
        }
        return null;
    }

}
