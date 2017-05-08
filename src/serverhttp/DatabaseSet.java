package serverhttp;

/**
 * Created by Corailla20 on 03/03/2016.
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Represents a JSON serializable ResultSet
 */
public class DatabaseSet {

    private ResultSet resultSet;
    private ResultSetMetaData metaData;

    public DatabaseSet(ResultSet resultSet) throws SQLException {
        this.resultSet = resultSet;
        this.metaData = resultSet.getMetaData();
    }

    public void closeResultSet() throws SQLException {
        resultSet.close();
    }
    
    

    /**
     * Generate JSON using the result set
     *
     * @return String
     */
    public String generateJSON() {
        JSONArray jsonArray = new JSONArray();

        try {
            while (this.resultSet.next()) {
                //System.out.println("Next Line");
                JSONObject jsonObject = new JSONObject();

                int i = 1;

                // Analyse the result set in order to create a JSON corresponding to the results
                while (i <= this.metaData.getColumnCount()) {
                    jsonObject.put(
                            metaData.getColumnName(i).toLowerCase(),
                            this.resultSet.getObject(i)
                    );

                    i++;
                }

                jsonArray.add(jsonObject);
            }

        /*
         * If there is a SQL exception here, it is because we are trying to fetch an INSERT / UPATE
         * We can ignore it
         */
        } catch (SQLException e) {
        }

        return jsonArray.toJSONString();
    }

}