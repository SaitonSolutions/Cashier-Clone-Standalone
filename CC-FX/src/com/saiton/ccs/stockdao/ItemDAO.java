
package com.saiton.ccs.stockdao;


import com.saiton.ccs.validations.FormatAndValidate;
import com.saiton.ccs.database.Starter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;

/**
 *
 * @author Miren
 */
public class ItemDAO {

    public static Starter star;//db connection
    Codec ORACLE_CODEC = new OracleCodec();
    private Logger log = Logger.getLogger(this.getClass());

    public String generateID() {

        Integer id = null;
        String cid = null;
        String final_id = null;
        if (star.con == null) {
            log.error("Databse connection failiure.");
            return null;
        } else {
            try {

                Statement st = star.con.createStatement();
                Statement ste = star.con.createStatement();
                ResultSet rs = st.executeQuery("SELECT MAX(id) as ID FROM item");

                while (rs.next()) {
                    id = rs.getInt("id");
                }
                ResultSet rss = ste.executeQuery("SELECT item_id FROM item WHERE id= " + id + "");

                while (rss.next()) {
                    cid = rss.getString("item_id");
                }

                if (id != 0) {
                    String original = cid.split("M")[1];
                    int i = Integer.parseInt(original) + 1;

                    if (i < 10) {
                        final_id = "ITM000" + i;
                    } else if (i >= 10 && i < 100) {
                        final_id = "ITM00" + i;
                    } else if (i >= 100 && i < 1000) {
                        final_id = "ITM0" + i;
                    } else if (i >= 1000 && i < 10000) {
                        final_id = "ITM" + i;
                    }
                    return final_id;

                } else {
                    return "ITM0001";
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | SQLException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {
                    log.error("Exception tag --> " + "Split character error");
                } else if (e instanceof NumberFormatException) {
                    log.error("Exception tag --> " + "Invalid number found in current id");
                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return null;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return null;
            }
        }
    }
    
    public String generateIDOOnDemand(int no) {

        Integer id = null;
        String cid = null;
        String final_id = null;
        if (star.con == null) {
            log.error("Databse connection failiure.");
            return null;
        } else {
            try {

                Statement st = star.con.createStatement();
                Statement ste = star.con.createStatement();
                ResultSet rs = st.executeQuery("SELECT MAX(id) as ID FROM item");

                while (rs.next()) {
                    id = rs.getInt("id");
                }
                ResultSet rss = ste.executeQuery("SELECT item_id FROM item WHERE id= " + id + "");

                while (rss.next()) {
                    cid = rss.getString("item_id");
                }

                if (id != 0) {
                    String original = cid.split("M")[1];
                    int i = Integer.parseInt(original) + no;

                    if (i < 10) {
                        final_id = "ITM000" + i;
                    } else if (i >= 10 && i < 100) {
                        final_id = "ITM00" + i;
                    } else if (i >= 100 && i < 1000) {
                        final_id = "ITM0" + i;
                    } else if (i >= 1000 && i < 10000) {
                        final_id = "ITM" + i;
                    }
                    return final_id;

                } else {
                    int i = no;
                    if (i < 10) {
                        final_id = "ITM000" + i;
                    } else if (i >= 10 && i < 100) {
                        final_id = "ITM00" + i;
                    } else if (i >= 100 && i < 1000) {
                        final_id = "ITM0" + i;
                    } else if (i >= 1000 && i < 10000) {
                        final_id = "ITM" + i;
                    }
                    return final_id;
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | SQLException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {
                    log.error("Exception tag --> " + "Split character error");
                } else if (e instanceof NumberFormatException) {
                    log.error("Exception tag --> " + "Invalid number found in current id");
                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return null;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return null;
            }
        }
    }

    public ArrayList<String> loadingCategory() {

        String category = null;
        ArrayList list = new ArrayList();

        if (star.con == null) {
            log.error(" Exception tag --> " + "Databse connection failiure. ");
            return null;

        } else {
            try {

                String query = "SELECT * FROM item_category ";
                PreparedStatement pstmt = star.con.prepareStatement(query);
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {
                    category = r.getString("category");
                    list.add(category);

                }

            } catch (ArrayIndexOutOfBoundsException | SQLException | NullPointerException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {
                    log.error("Exception tag --> " + "Invalid entry location for list");
                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement");
                } else if (e instanceof NullPointerException) {
                    log.error("Exception tag --> " + "Empty entry for list");
                }
                return null;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return null;
            }
        }
        return list;
    }

    public boolean checkingCategoryAvailability(String desc) {

        String encodedDesc = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, desc);
        boolean available = false;

        if (star.con == null) {

            log.error("Exception tag --> " + "Databse connection failiure. ");

        } else {
            try {

                String query = "SELECT * FROM item_category i where i.category= ? ";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedDesc);
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {
                    available = true;
                }
                if (available == false) {
                    insertCategory(desc);
                }

            } catch (NullPointerException | SQLException e) {
                if (e instanceof NullPointerException) {
                    log.error("Exception tag --> " + "Empty entry passed");

                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement");
                }
                return false;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
        return available;
    }

    public boolean insertCategory(String desc) {
        String encodedDesc = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, desc);
        if (star.con == null) {
            log.info(" Exception tag --> " + "Databse connection failiure. ");
            return false;
        } else {
            try {
                String sql = "INSERT INTO item_category (`category`) VALUES (?);";

                PreparedStatement stmt = Starter.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, encodedDesc);
                int val = stmt.executeUpdate();
                if (val == 1) {
                    return true;
                } else {
                    return false;
                }

            } catch (SQLException e) {
                log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                return false;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }

    }

    public boolean deleteCategory(String Desc) {
        if (star.con == null) {
            log.info(" Exception tag --> " + "Databse connection failiure. ");
            return false;
        } else {
            String encodedDesc = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, Desc);
            try {
                String sql = "DELETE FROM item_category where `category`= ? ";
                PreparedStatement stmt = Starter.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, encodedDesc);
                int val = stmt.executeUpdate();
                if (val == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                return false;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
    }

    public boolean checkingItemAvailability(String itemId) {

        String encodedItemId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemId);
        boolean available = false;

        if (star.con == null) {

            log.error("Exception tag --> " + "Databse connection failiure. ");

        } else {
            try {

                String query = "SELECT * FROM item where item_id= ? ";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedItemId);
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {
                    available = true;
                }

            } catch (NullPointerException | SQLException e) {
                if (e instanceof NullPointerException) {
                    log.error("Exception tag --> " + "Empty entry passed");

                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement");
                }
                return false;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
        return available;
    }

    public boolean checkingItemNameAvailability(String itemName) {

//        String encodedItemName = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemName);
String encodedItemName = itemName;
        boolean available = false;

        if (star.con == null) {

            log.error("Exception tag --> " + "Databse connection failiure. ");

        } else {
            try {
                

                String query = "SELECT * FROM item  where item_name= ?";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedItemName);

                ResultSet r = pstmt.executeQuery();

                while (r.next()) {
                    available = true;
                }

            } catch (NullPointerException | SQLException e) {
                if (e instanceof NullPointerException) {
                    log.error("Exception tag --> " + "Empty entry passed");

                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement");
                }
                return false;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
        return available;
    }

    public String getUserName(String ItemId) {

        String encodedItemId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, ItemId);
        String userName=null;
        if (star.con == null) {
            log.error("Databse connection failiure.");
            return null;
        } else {
            try {

                String query = "select u.user_name "
                        + "from item i "
                        + "left join user u "
                        + "on u.eid=i.user_id "
                        + "where i.item_id=? ";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedItemId);
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {
                    userName = r.getString("u.user_name");
                }
               

                return userName;
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | SQLException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {
                    log.error("Exception tag --> " + "Split character error");
                } else if (e instanceof NumberFormatException) {
                    log.error("Exception tag --> " + "Invalid number found in current id");
                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return null;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return null;
            }
        }
    }
    
    public ArrayList<ArrayList<String>> searchItemDetails(String item) {

        String encodedItem = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, item);

        String itemId = null;
        String itemName = null;
        String qty = null;

        String price = null;


        ArrayList<ArrayList<String>> Mainlist = new ArrayList<ArrayList<String>>();

        if (star.con == null) {

            log.info(" Exception tag --> " + "Databse connection failiure. ");
            return null;

        } else {
            try {

                String query = "SELECT * "
                        + "From item "
                        + "Where item_id LIKE ? or item_name LIKE ? ";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedItem + "%");
                pstmt.setString(2, encodedItem + "%");
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {

                    ArrayList<String> list = new ArrayList<String>();

                    itemId = r.getString("item_id");
                    itemName = r.getString("item_name");

                            
                    list.add(itemId);
                    list.add(itemName);

                    
                    Mainlist.add(list);

                }

            } catch (ArrayIndexOutOfBoundsException | SQLException | NullPointerException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {

                    log.error("Exception tag --> " + "Invalid entry location for list");
            
        } else if (e instanceof SQLException) {

                    log.error("Exception tag --> " + "Invalid sql statement"+e);

                } else if (e instanceof NullPointerException) {

                    log.error("Exception tag --> " + "Empty entry for list");

                }
                return null;
            } catch (Exception e) {

                log.error("Exception tag --> " + "Error");

                return null;
            }
        }
        return Mainlist;
    }
    
    public ArrayList<ArrayList<String>> searchItemBatchDetails(String item) {

        String encodedItem = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, item);

        String batchNo = null;
        String price = null;

        ArrayList<ArrayList<String>> Mainlist = new ArrayList<ArrayList<String>>();

        if (star.con == null) {

            log.info(" Exception tag --> " + "Databse connection failiure. ");
            return null;

        } else {
            try {

                String query = "SELECT * "
                        + "From item_sub "
                        + "Where item_id LIKE ? ";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedItem + "%");
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {

                    ArrayList<String> list = new ArrayList<String>();

                    batchNo = r.getString("batch_no");
                    price = r.getString("price");
                            
                    list.add(batchNo);
                    list.add(price);
      
                    Mainlist.add(list);

                }

            } catch (ArrayIndexOutOfBoundsException | SQLException | NullPointerException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {

                    log.error("Exception tag --> " + "Invalid entry location for list");

                } else if (e instanceof SQLException) {

                    log.error("Exception tag --> " + "Invalid sql statement"+e);

                } else if (e instanceof NullPointerException) {

                    log.error("Exception tag --> " + "Empty entry for list");

                }
                return null;
            } catch (Exception e) {

                log.error("Exception tag --> " + "Error");

                return null;
            }
        }
        return Mainlist;
    }
        
    public String getPrice(String item,String batchNo) {

        String encodedItem = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, item);
        String encodedBatchNo = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, batchNo);

        String price = null;

        if (star.con == null) {

            log.info(" Exception tag --> " + "Databse connection failiure. ");
            return null;

        } else {
            try {

                String query = "SELECT * "
                        + "From item_sub "
                        + "Where item_id = ? and batch_no=? ";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedItem);
                pstmt.setString(2, encodedBatchNo);
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {
                    price = r.getString("price");
                }

            } catch (ArrayIndexOutOfBoundsException | SQLException | NullPointerException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {

                    log.error("Exception tag --> " + "Invalid entry location for list");

                } else if (e instanceof SQLException) {

                    log.error("Exception tag --> " + "Invalid sql statement"+e);

                } else if (e instanceof NullPointerException) {

                    log.error("Exception tag --> " + "Empty entry for list");

                }
                return null;
            } catch (Exception e) {

                log.error("Exception tag --> " + "Error");

                return null;
            }
        }
        return price;
    }    
    
    public boolean deleteItem(String item) {
        
        
        
        String encodedItem = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, item);
        if (star.con == null) {
            log.info(" Exception tag --> " + "Databse connection failiure. ");
            return false;
        } else {
            try {
                String sql = "DELETE FROM item WHERE item_id='"+encodedItem+"' ";
                PreparedStatement stmt = star.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//                stmt.setString(1, encodedItem);
                stmt.executeUpdate();
            } catch (SQLException e) {

                if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return false;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
            return true;
        }
    }
        
    public boolean additem(String itemId,
            String itemName,

            String userId,

            String batchNo,
            Double price) {
        String encodedItemId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemId);
//        String encodedItemName = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemName);
        String encodedItemName = itemName;

        String encodeduserId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, userId);

        String encodedBatchNo = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, batchNo);
        boolean available = false;
        boolean available1 = false;
        boolean value = false;

        if (star.con == null) {

            log.error("Exception tag --> " + "Databse connection failiure. ");

        } else {
            try {

                String query = "SELECT * FROM item where item_id= ? ";

                PreparedStatement pstmt = star.con.prepareStatement(query);
                pstmt.setString(1, encodedItemId);
                ResultSet r = pstmt.executeQuery();

                while (r.next()) {
                    value=updateItems(itemId,itemName,userId);
                    available=true;
                }
                if (available == false) {
                    value=insertItems(itemId,itemName,userId);
                }
                
                String query1 = "SELECT * FROM item_sub where item_id=? and batch_no=? ";

                PreparedStatement pstmt1 = star.con.prepareStatement(query1);
                pstmt1.setString(1, encodedItemId);
                pstmt1.setString(2, encodedBatchNo);
                ResultSet r1 = pstmt1.executeQuery();

                while (r1.next()) {
                    available1=true;
                    value=updateItemsSub(itemId,batchNo,price);
                }
                if (available1 == false) {
                    value=insertItemsSub(itemId,batchNo,price);
                }

            } catch (NullPointerException | SQLException e) {
                if (e instanceof NullPointerException) {
                    log.error("Exception tag --> " + "Empty entry passed");

                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement");
                }
                return false;
            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
        return value;
    }
        
    public Boolean insertItems(
            String itemId,
            String itemName,

            String userId

    ) {
        String encodedItemId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemId);
//        String encodedItemName = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemName);
        String encodedItemName = itemName;

        String encodeduserId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, userId);


        if (star.con == null) {
            log.error("Databse connection failiure.");
            return false;
        } else {
            try {

                PreparedStatement ps = star.con.prepareStatement("INSERT INTO item(`item_id`, `item_name`, `user_id`) "
                        + "VALUES(?,?,?)");

                ps.setString(1, encodedItemId);
                ps.setString(2, encodedItemName);

                ps.setString(3, encodeduserId);

                int val = ps.executeUpdate();

                if (val == 1) {
                    return true;
                } else {
                    return false;
                }

            } catch (SQLException e) {

                if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return false;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
    }
    
    public Boolean updateItems(
            String itemId,
            String itemName,

            String userId

    ) {
        String encodedItemId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemId);
//        String encodedItemName = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemName);
String encodedItemName = itemName;
        String encodeduserId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, userId);


        if (star.con == null) {
            log.error("Databse connection failiure.");
            return false;
        } else {
            try {

                PreparedStatement ps = star.con.prepareStatement("UPDATE item SET `item_name`=? , "
                        + "`user_id`=? WHERE `item_id`=? ");

                
                ps.setString(1, encodedItemName);

                ps.setString(2, encodeduserId);
                ps.setString(3, encodedItemId);

                int val = ps.executeUpdate();

                if (val == 1) {
                    return true;
                } else {
                    return false;
                }

            } catch (SQLException e) {

                if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return false;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
    }
    
    public Boolean insertItemsSub(
            String itemId,
            String batchNo,
            Double price) {
        String encodedItemId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemId);
        String encodedBatchNo = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, batchNo);

        if (star.con == null) {
            log.error("Databse connection failiure.");
            return false;
        } else {
            try {

                PreparedStatement ps = star.con.prepareStatement("INSERT INTO item_sub(`item_id`, `batch_no`, `price`) "
                        + "VALUES(?,?,?)");

                ps.setString(1, encodedItemId);
                ps.setString(2, encodedBatchNo);
                ps.setDouble(3, price);

                int val = ps.executeUpdate();

                if (val == 1) {
                    return true;
                } else {
                    return false;
                }

            } catch (SQLException e) {

                if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return false;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
    }
    
    public Boolean updateItemsSub(
            String itemId,
            String batchNo,
            Double price) {
        String encodedItemId = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, itemId);
        String encodedBatchNo = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, batchNo);

        if (star.con == null) {
            log.error("Databse connection failiure.");
            return false;
        } else {
            try {

                PreparedStatement ps = star.con.prepareStatement("UPDATE item_sub SET `price`=? "
                        + "WHERE `item_id`=? and `batch_no`=? ");

                ps.setDouble(1, price);
                ps.setString(2, encodedItemId);
                ps.setString(3, encodedBatchNo);
                
                
                int val = ps.executeUpdate();

                if (val == 1) {
                    return true;
                } else {
                    return false;
                }

            } catch (SQLException e) {

                if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return false;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return false;
            }
        }
    }
    
    public String generateBatchID(String ItemNo) {
         String encodedItemNo = ESAPI.encoder().encodeForSQL(ORACLE_CODEC, ItemNo);
        Integer id = null;
        String cid = null;
        String final_id = null;
        if (star.con == null) {
            log.error("Databse connection failiure.");
            return null;
        } else {
            try {

                Statement st = star.con.createStatement();
                Statement ste = star.con.createStatement();
                ResultSet rs = st.executeQuery("SELECT MAX(id) as ID FROM item_sub "
                        + "where item_id= '" + encodedItemNo + "' ");

                while (rs.next()) {
                    id = rs.getInt("id");
                }
                ResultSet rss = ste.executeQuery("SELECT batch_no FROM item_sub WHERE id= " + id + "");

                while (rss.next()) {
                    cid = rss.getString("batch_no");
                }

                if (id != 0) {
                    String original = cid.split("T")[1];
                    int i = Integer.parseInt(original) + 1;

                    if (i < 10) {
                        final_id = "BAT000" + i;
                    } else if (i >= 10 && i < 100) {
                        final_id = "BAT00" + i;
                    } else if (i >= 100 && i < 1000) {
                        final_id = "BAT0" + i;
                    } else if (i >= 1000 && i < 10000) {
                        final_id = "BAT" + i;
                    }
                    return final_id;

                } else {
                    return "BAT0001";
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | SQLException e) {

                if (e instanceof ArrayIndexOutOfBoundsException) {
                    log.error("Exception tag --> " + "Split character error");
                } else if (e instanceof NumberFormatException) {
                    log.error("Exception tag --> " + "Invalid number found in current id");
                } else if (e instanceof SQLException) {
                    log.error("Exception tag --> " + "Invalid sql statement " + e.getMessage());
                }
                return null;

            } catch (Exception e) {
                log.error("Exception tag --> " + "Error");
                return null;
            }
        }
    }

}
