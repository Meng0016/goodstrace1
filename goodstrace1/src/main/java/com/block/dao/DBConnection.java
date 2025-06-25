package com.block.dao;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static final Logger log = LoggerFactory.getLogger(DBConnection.class);
    private static Connection conn = null;
    private static final String JDBC_DRIVER;
    private static final String DB_URL;
    private static final String USER_NAME;
    private static final String PASSWORD;

    static {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            // 从类路径下加载配置文件
            inputStream = DBConnection.class.getClassLoader().getResourceAsStream("application.yml");
            if (inputStream != null) {
                properties.load(inputStream);
                JDBC_DRIVER = properties.getProperty("db.driverClassName");
                DB_URL = properties.getProperty("db.url");
                USER_NAME = properties.getProperty("db.username");
                PASSWORD = properties.getProperty("db.password");
            } else {
                throw new IOException("Property file 'application.yml' not found in the classpath.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void initialize() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            createBlockInfoTable();
            createGoodsInfoTable();
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Database initialization failed", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private static void createBlockInfoTable() {
        String sql = "CREATE TABLE IF NOT EXISTS BlockInfo (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "transactionHash VARCHAR(255) NOT NULL, " +
                "transactionIndex VARCHAR(64), " +
                "root VARCHAR(255), " +
                "blockNumber VARCHAR(64) NOT NULL, " +
                "blockHash VARCHAR(255) NOT NULL, " +
                "fromAddress VARCHAR(255), " +
                "toAddress VARCHAR(255), " +
                "gasUsed INT, " +
                "contractAddress VARCHAR(255), " +
                "logsBloom TEXT, " +
                "status VARCHAR(64), " +
                "statusMsg VARCHAR(255), " +
                "input TEXT, " +
                "output TEXT, " +
                "message TEXT, " +
                "statusOK BOOLEAN, " +
                "INDEX (blockNumber))";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("BlockInfo table created successfully.");
        } catch (SQLException e) {
            log.error("Failed to create BlockInfo table", e);
        }
    }

    private static void createGoodsInfoTable() {
        String sql = "CREATE TABLE IF NOT EXISTS goodsInfo (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "traceNumber VARCHAR(255), " +
                "blockNumber VARCHAR(64), " +
                "type VARCHAR(64), " +
                "FOREIGN KEY (blockNumber) REFERENCES BlockInfo (blockNumber))";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("goodsInfo table created successfully.");
        } catch (SQLException e) {
            log.error("Failed to create goodsInfo table", e);
        }
    }

    public static void addBlockInfo(JSONObject receipt) throws SQLException {
        String sql = "INSERT INTO BlockInfo (" +
                "transactionHash, transactionIndex, root, blockNumber, blockHash, " +
                "fromAddress, toAddress, gasUsed, contractAddress, logsBloom, " +
                "status, statusMsg, input, output, message, statusOK) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, receipt.getString("transactionHash"));
            stmt.setString(2, receipt.getString("transactionIndex"));
            stmt.setString(3, receipt.getString("root"));
            stmt.setString(4, receipt.getString("blockNumber"));
            stmt.setString(5, receipt.getString("blockHash"));
            stmt.setString(6, receipt.getString("from"));
            stmt.setString(7, receipt.getString("to"));
            stmt.setInt(8, receipt.getInteger("gasUsed"));
            stmt.setString(9, receipt.getString("contractAddress"));
            stmt.setString(10, receipt.getString("logsBloom"));
            stmt.setString(11, receipt.getString("status"));
            stmt.setString(12, receipt.getString("statusMsg"));
            stmt.setString(13, receipt.getString("input"));
            stmt.setString(14, receipt.getString("output"));
            stmt.setString(15, receipt.getString("message"));
            stmt.setBoolean(16, receipt.getBoolean("statusOK"));
            stmt.executeUpdate();
            log.info("BlockInfo added successfully.");
        } catch (SQLException e) {
            log.error("Failed to add BlockInfo", e);
            throw e;
        }
    }

    public static void addGoodsInfo(String traceNumber, String blockNumber, String type) throws SQLException {
        String sql = "INSERT INTO goodsInfo (traceNumber, blockNumber, type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, traceNumber);
            stmt.setString(2, blockNumber);
            stmt.setString(3, type);
            stmt.executeUpdate();
            log.info("Data added to goodsInfo table successfully.");
        } catch (SQLException e) {
            log.error("Failed to add data to goodsInfo table", e);
            throw e;
        }
    }

    // 辅助方法, 将ResultSet转换为JSONObject
    private static JSONObject convertToJSONObject(ResultSet rs) throws SQLException {
        JSONObject jsonObj = new JSONObject();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            jsonObj.put(metaData.getColumnName(i), rs.getObject(i));
        }
        return jsonObj;
    }

    // 通过注册id和类型获取区块信息,并返回JSONObject
    public static JSONObject getBlockInfoByTraceNumber(String traceNumber, String type) throws SQLException {
        String query = "SELECT b.* FROM BlockInfo b JOIN goodsInfo g ON b.blockNumber = " +
                "g.blockNumber WHERE g.traceNumber =? AND g.type=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, traceNumber);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return convertToJSONObject(rs);
            }
        }
        return new JSONObject(); // 返回空的JSONObject
    }
}
