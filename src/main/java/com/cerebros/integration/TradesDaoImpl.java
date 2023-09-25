package com.cerebros.integration;

import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.exceptions.DatabaseException;
import com.cerebros.models.Order;
import com.cerebros.models.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository("tradesDaoImpl")
public class TradesDaoImpl implements TradesDao{
    @Autowired
    private TradesMapper mapper;
    public TradesDaoImpl() {

    }

    @Override
    public List<Trade> getTrades(String clientId) throws SQLException, ClientNotFoundException {
        List<Trade> Trades= mapper.getTrades(clientId);
        if(Trades.isEmpty()){
            throw new DatabaseException("Client Invalid or no Trade history");
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
            throw new DatabaseException("add Trade failed",e);
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
