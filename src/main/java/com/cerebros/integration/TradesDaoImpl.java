package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Order;
import com.cerebros.models.Portfolio;
import com.cerebros.models.Trade;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TradesDaoImpl implements TradesDao{
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    private DataSource dataSource;

    public TradesDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Trade> getTrades(String clientId) throws SQLException, ClientNotFoundException {
//        return null;
        String sql= "select t.tradeid, t.quantity, t.executionprice, t.direction , t.cashvalue,t.clientId, t.instrumentid, t.orderId, o.quantity, o.targetPrice, o.direction, o.clientId, o.instrumentId from cerebros_trades t inner join cerebros_instruments i on t.instrumentid= i.instrumentid inner join cerebros_orders o on t.orderid =o.orderid where t.clientID=? ORDER BY t.tradeid FETCH FIRST 100 ROWS ONLY";
        List<Trade> Trades= new ArrayList<>();
        PreparedStatement statement= null;
        Connection conn= dataSource.getConnection();
        try{
            statement = conn.prepareStatement(sql);
            statement.setString(1,clientId);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                logger.error("Client with ID " + clientId + " not found in the database");
                throw new ClientNotFoundException("Client with ID " + clientId + " not found");
            }
            do {
                String tradeId= rs.getString(1);
                BigDecimal quantity = rs.getBigDecimal(2).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal exPrice = rs.getBigDecimal(3).setScale(2, RoundingMode.HALF_EVEN);
                String direction= rs.getString(4);
                BigDecimal cashValue = rs.getBigDecimal(5).setScale(2, RoundingMode.HALF_EVEN);
                String clientID= rs.getString(6);
                String instrumentId= rs.getString(7);
                String orderId= rs.getString(8);
                BigDecimal orderQuantity= rs.getBigDecimal(9);
                BigDecimal targetPrice= rs.getBigDecimal(10);
                String orderDirection= rs.getString(11);
                Order order= new Order(orderId, orderQuantity, targetPrice, orderDirection, clientID,instrumentId);
                Trade trade= new Trade(tradeId,quantity,exPrice,direction,cashValue,clientID,instrumentId,order);
                Trades.add(trade);
            }
            while (rs.next());
            logger.info("Client Trade successfully retrieved for ID " + clientId);

        }catch (SQLException e){
            logger.error("get Trade failed", e);
            throw new DatabaseException("get Trade failed",e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("can't close connection", e);
                }
            }
        }
        return  Trades;
    }

    @Override
    public void addTrade(Trade trade, String clientId) {

        Connection conn = null;
        PreparedStatement orderStatement = null;
        PreparedStatement tradeStatement = null;
        Order order= trade.getOrder();

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start a transaction

            // Step 1: Insert data into Cerebros_Orders table
            String orderSql = "INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";
            orderStatement = conn.prepareStatement(orderSql);
            orderStatement.setString(1, order.getOrderId());
            orderStatement.setString(2, order.getClientId());
            orderStatement.setString(3, order.getInsturmentId());
            orderStatement.setString(4, order.getDirection());
            orderStatement.setBigDecimal(5, order.getQuantity());
            orderStatement.setBigDecimal(6, order.getTargetPrice());
            orderStatement.setTimestamp(7, new Timestamp(order.getPlacedTime().getTime()));
            orderStatement.executeUpdate();

            // Step 2: Retrieve the generated order ID
            // Assuming orderId is auto-generated, you may need to fetch it based on your database's auto-increment mechanism.
            // If you're using a sequence or some other method, adjust this part accordingly.

            // Step 3: Insert data into Cerebros_Trades table using the retrieved order ID
            String tradeSql = "INSERT INTO Cerebros_Trades (tradeId, clientId, instrumentId, orderId, direction, quantity, executionPrice, cashValue, executedTimestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            tradeStatement = conn.prepareStatement(tradeSql);
            tradeStatement.setString(1, trade.getTradeId());
            tradeStatement.setString(2, trade.getClientid());
            tradeStatement.setString(3, trade.getInstrumentId());
            tradeStatement.setString(4, order.getOrderId()); // Use the retrieved order ID
            tradeStatement.setString(5, trade.getDirection());
            tradeStatement.setBigDecimal(6, trade.getQuantity());
            tradeStatement.setBigDecimal(7, trade.getExecutionPrice());
            tradeStatement.setBigDecimal(8, trade.getCashValue());
            tradeStatement.setTimestamp(9, new Timestamp(trade.getExecutedTime().getTime()));
            tradeStatement.executeUpdate();


        } catch (SQLException e){
            logger.error("add Trade failed", e);
            throw new DatabaseException("add Trade failed",e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("can't close connection", e);
                }
            }
        }
    }

}
