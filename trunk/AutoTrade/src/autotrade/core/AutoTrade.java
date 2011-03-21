/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package autotrade.core;

import autotrade.core.database.AutoTradeDatabaseManagement;
import autotrade.core.technicalanalysismethod.SimpleMovingAverage;
import autotrade.core.technicalanalysismethod.TechnicalAnalysisMethod;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.time.*;
import java.util.ArrayList;

/**
 *
 * @author Dinh
 */
public class AutoTrade {

    public static ArrayList<String> LIST_ALL_JSC_SYMBOL = getAllJSCSymbol();
    public static ArrayList<User> LIST_ALL_USER = getAllUsers();
    
    public static long earliestTime = AutoTrade.getEarliestTimeInDatabase();
    public static long latestTime = AutoTrade.getLatestTimeInDatabase();

    public static void addNewUser(String userName, int typeId, int tamiID, double cashRemain) {
        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();

            String sqlStatement = "INSERT user VALUES(";
            sqlStatement += "NULL ,";
            sqlStatement += "'" + userName + "',";
            sqlStatement += "'" + typeId + "',";
            sqlStatement += "'" + tamiID + "',";
            sqlStatement += "'" + cashRemain + "',";
            sqlStatement += "'" + 1 + "');";

            statement.executeUpdate(sqlStatement);

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void addNewTAMI(int tamID, int period) {
        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();

            String sqlStatement = "INSERT `technical_analysis_method_instance` VALUES(";
            sqlStatement += "NULL ,";
            sqlStatement += "'" + tamID + "',";
            sqlStatement += "'" + period + "');";
           
            statement.executeUpdate(sqlStatement);

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static int getTAMIID(int tamID, int period) {
        int tamiID = 0;
        
        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `technical_analysis_method_instance` "
                    + "WHERE `technical_analysis_method_id` = '" + tamID + "'"
                    + "AND `period` = '" + period + "'");
            resultSet.next();
            tamiID = resultSet.getInt("id");
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tamiID;
    }

    public static int getTAMID(String TAMName) {
        int tamID = 0;

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `technical_analysis_method` "
                    + "WHERE `name` LIKE '" + TAMName + "'");
                    
            resultSet.next();
            tamID = resultSet.getInt("id");
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tamID;
        
    }

    public static int getUserTypeID(String userTypeName) {
        int userTypeID = 0;

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `user_type` "
                    + "WHERE `user_type_name` LIKE '" + userTypeName + "'");

            resultSet.next();
            userTypeID = resultSet.getInt("user_type_id");
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userTypeID;
    }

    public static String getUserTypeName(int userTypeID) {
        String userTypeName = "";

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `user_type` "
                    + "WHERE `user_type_id` = '" + userTypeID + "'");

            resultSet.next();
            userTypeName = resultSet.getString("user_type_name");
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userTypeName;
    }

    public static long getLatestTimeInDatabase() {
        long latestTime = 0;

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(date) FROM stock_price_daily");
            resultSet.next();
            Date date = resultSet.getDate(1);
            latestTime = date.getTime();
            
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return latestTime;
    }

    public static long getEarliestTimeInDatabase() {
        long earliestTime = 0;

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT min(date) FROM stock_price_daily");
            resultSet.next();
            Date date = resultSet.getDate(1);
            earliestTime = date.getTime();

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return earliestTime;
    }


    public static ArrayList<String> getAllJSCSymbol() {
        ArrayList<String> allJSCSymbol = new ArrayList<String>();

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM joint_stock_company");
            while (resultSet.next()) {
                String symbol = resultSet.getString("symbol");
                allJSCSymbol.add(symbol);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return allJSCSymbol;
    }

    public static TimeSeries getPriceSeries(long startTime, long endTime, String companySymbol, String priceType) {
        Date startDate = new Date(startTime);
        Date currentDate = new Date(endTime);

        TimeSeries priceSeries = new TimeSeries("Price Series");
        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * "
                    + "FROM `stock_price_daily`"
                    + "WHERE `symbol` LIKE '" + companySymbol + "' "
                    + "AND `date` <= '" + currentDate.toString() + "' "
                    + "AND `date` >= '" + startDate.toString() + "'");

            while (resultSet.next()) {
                Double price = resultSet.getDouble(priceType);
                Date date = resultSet.getDate("date");
                priceSeries.add(new Day(date), price);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return priceSeries;
    }

    public static ArrayList<String> getAllJSCSymbolAtThisTime(Date current_date) {
        ArrayList<String> allJSCSymbol = new ArrayList<String>();

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM stock_price_daily WHERE date LIKE '" + current_date.toString() + "'");
            while (resultSet.next()) {
                String symbol = resultSet.getString("symbol");
                allJSCSymbol.add(symbol);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return allJSCSymbol;
    }

    public static TimeSeries getVolumeSeries(long startTime, long endTime, String companySymbol) {
        Date startDate = new Date(startTime);
        Date currentDate = new Date(endTime);

        TimeSeries volumeSeries = new TimeSeries("Volume");
        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * "
                    + "FROM `stock_price_daily`"
                    + "WHERE `symbol` LIKE '" + companySymbol + "' "
                    + "AND `date` <= '" + currentDate.toString() + "' "
                    + "AND `date` >= '" + startDate.toString() + "'");

//            Double volume1, volume2;
//            Date date1, date2;
//            long oneDay = 86400000; //milisecond
//
//            if (resultSet.next()) {
//                volume1 = resultSet.getDouble("volume");
//                date1 = resultSet.getDate("date");
//                volumeSeries.add(new Day(date1), volume1);
//
//                while (resultSet.next()) {
//                    volume2 = resultSet.getDouble("volume");
//                    date2 = resultSet.getDate("date");
//                    for (Date date = new Date(date1.getTime() + oneDay); date.compareTo(date2) < 0; date = new Date(date.getTime() + oneDay)) {
//                        volumeSeries.add(new Day(date), volume1);
//                    }
//
//                    volumeSeries.add(new Day(date2), volume2);
//                    date1 = date2;
//                    volume1 = volume2;
//                }
//            }

            Double volume;
            Date date;

            while (resultSet.next()) {
                volume = resultSet.getDouble("volume");
                date = resultSet.getDate("date");
                volumeSeries.add(new Day(date), volume);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return volumeSeries;
    }
    
    public static ArrayList<String> getListUserTypeName() {
        ArrayList<String> listUserTypeName = new ArrayList<String>();
        
        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * "
                    + "FROM `user_type`");

            while (resultSet.next()) {
                String userTypeName = resultSet.getString("user_type_name");
                listUserTypeName.add(userTypeName);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return listUserTypeName;
    }

        public static ArrayList<String> getListTechnicalAnalysisMethod() {
        ArrayList<String> listTechnicalAnalysisMethod = new ArrayList<String>();

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * "
                    + "FROM `technical_analysis_method`");

            while (resultSet.next()) {
                String technical_analysis_method_name = resultSet.getString("name");
                listTechnicalAnalysisMethod.add(technical_analysis_method_name);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listTechnicalAnalysisMethod;
    }

    public static User getUser(String userName) {
        User user = new User();

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * "
                    + "FROM `user` "
                    + "WHERE `name` LIKE '" + userName +"'");

            while (resultSet.next()) {
                user.setUserID(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("name"));
                user.setTypeID(resultSet.getInt("type_id"));
                user.setTypeName(getUserTypeName(user.getTypeID()));
                user.setCash_remain(resultSet.getDouble("cash_remain"));
                user.setActive(resultSet.getBoolean("active"));
                user.setTechnicalAnalysisMethod(getTAMI(resultSet.getInt("technical_analysis_method_instance_id")));
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    private static ArrayList<User> getAllUsers() {
        ArrayList<User> allUser = new ArrayList<User>();

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * "
                    + "FROM `user`");

            while (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("name"));
                user.setTypeID(resultSet.getInt("type_id"));
                user.setTypeName(getUserTypeName(user.getTypeID()));
                user.setCash_remain(resultSet.getDouble("cash_remain"));
                user.setActive(resultSet.getBoolean("active"));
                user.setTechnicalAnalysisMethod(getTAMI(resultSet.getInt("technical_analysis_method_instance_id")));

                allUser.add(user);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return allUser;
    }

    private static TechnicalAnalysisMethod getTAMI(int tamiID) {
        TechnicalAnalysisMethod tami = null;

        try {
            Connection conn = AutoTradeDatabaseManagement.getConnectionWithDatabase();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * "
                    + "FROM `technical_analysis_method_instance` AS A, `technical_analysis_method` AS B "
                    + "WHERE A.technical_analysis_method_id = B.id "
                    + "AND A.id = '" + tamiID +"'");

            while (resultSet.next()) {
                String className = resultSet.getString("class_name");
                int period = resultSet.getInt("period");
                String name = resultSet.getString("name");
                
                tami = (TechnicalAnalysisMethod) Class.forName(className).newInstance();
                tami.setName(name);
                
                if (tami instanceof SimpleMovingAverage) {
                    ((SimpleMovingAverage) tami).setPeriod(period);
                }
            }

            conn.close();
        } catch (InstantiationException ex) {
            Logger.getLogger(AutoTrade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AutoTrade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AutoTrade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return tami;
    }

        public static Object[][] getTableData() {
        Object[][] data = new Object[AutoTrade.LIST_ALL_USER.size()][6];

        for (int i = 0; i < AutoTrade.LIST_ALL_USER.size(); ++i) {
            User user = AutoTrade.LIST_ALL_USER.get(i);
            data[i][0] = new Integer(user.getUserID());
            data[i][1] = user.getUserName();
            data[i][2] = user.getTypeName();
            data[i][3] = user.getTechnicalAnalysisMethod().getName();
            data[i][4] = new Double(user.getCash_remain());
            data[i][5] = user.isActive();
        }

        return data;
    }

}