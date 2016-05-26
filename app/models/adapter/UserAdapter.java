package models.adapter;
import java.sql.*;

public class UserAdapter {
    private Connection conn_source;
    private Connection conn_dest;
    public void connectDataBase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn_source=DriverManager.getConnection("jdbc:mysql://202.121.178.157:3306/cnmooc?useUnicode=true&characterEncoding=GBK&rewriteBatchedStatements=true","root","1234");
    }
    public void connectDest() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn_dest=DriverManager.getConnection("jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK","root","1234");
    }

    public UserAdapter() throws ClassNotFoundException, SQLException {
        connectDataBase();
        connectDest();

    }

    public void user_basic_import() throws SQLException {
        Statement stmt_source=conn_source.createStatement();
        Statement stmt_dest=conn_dest.createStatement();
        String sql = "create table IF NOT EXISTS user_basic ("+
                "user_id bigint(20) ,"
                + "user_name  varchar(50) character set gbk,"
                + "in_school int(2) ,"
                + "organization varchar(100) character set gbk,"
                + "country varchar(100) character set gbk,"
                + "province varchar(100) character set gbk,"
                + "student_no varchar(50) character set gbk,"
                + "role int(11),"
                + "platform varchar(50) character set gbk )";
        stmt_dest.execute(sql);
        ResultSet res;
        sql="select coeus_user_id, user_name, in_school, mooc_user_info.school_name, t1.region_name, t2.region_name, student_no, dignity from mooc_user_info "+
                "left join mooc_school on mooc_user_info.school_id = mooc_school.school_id "
                + "left join coeus_region t1 on mooc_school.country_code =t1.region_code "
                + "left join coeus_region t2 on mooc_school.province_code =t2.region_code ";
        res=stmt_source.executeQuery(sql);
        PreparedStatement  pstmt = conn_dest.prepareStatement("insert into user_basic (user_id, user_name,in_school,organization,country, province,student_no,role,platform)"+
                " values (?,?,?,?,?,?,?,?,?)");
        int batchSize=1;
        int current_size=0;
        while(res.next()) {
            if(current_size==batchSize) {
                current_size=0;
                pstmt.executeBatch();
                pstmt.clearBatch();
            }
            for (int i = 1; i <10; i++) {
                if(i==9) {
                    pstmt.setString(i, "haodaxue");
                    continue;
                }
                if(res.getString(i)==null) {
                    if(i==1||i==3||i==8)
                        pstmt.setString(i, "0");
                    else
                        pstmt.setString(i, "");
                    continue;
                }
                pstmt.setString(i, res.getString(i));
            }
            pstmt.addBatch();
            current_size++;
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserAdapter adp=new UserAdapter();
        adp.user_basic_import();
    }
}
