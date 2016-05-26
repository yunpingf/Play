package models.adapter;

import java.sql.*;

public class TestAdapter {
    private Connection conn_source;
    private Connection conn_dest;

    public void connectDataBase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn_source = DriverManager.getConnection("jdbc:mysql://202.121.178.157:3306/cnmooc?useUnicode=true&characterEncoding=GBK&rewriteBatchedStatements=true", "root", "1234");
    }

    public void connectDest() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn_dest = DriverManager.getConnection("jdbc:mysql://localhost:3306/cnmooc?useUnicode=true&characterEncoding=GBK", "root", "1234");
    }

    public TestAdapter() throws ClassNotFoundException, SQLException {
        connectDataBase();
        connectDest();
    }

    public void test_submit_import() throws SQLException {
        Statement stmt_source = conn_source.createStatement();
        Statement stmt_dest = conn_dest.createStatement();
        String sql = "create table IF NOT EXISTS test_submit (" +
                "test_submit_id varchar(50)  character set gbk ,"
                + "test_id  varchar(50) character set gbk,"
                + "user_id varchar(50) character set gbk,"
                + "usetime int(11),"
                + "submitTime datetime,"
                + "score int(11),"
                + "platform varchar(50) character set gbk )";
        stmt_dest.execute(sql);
        ResultSet res;
        sql = "select submit_id, test_paper_id, user_id, use_time, end_time, readover_score/100 from core_test_submit";
        res = stmt_source.executeQuery(sql);
        PreparedStatement pstmt = conn_dest.prepareStatement("insert into test_submit (test_submit_id, test_id,user_id,usetime,submitTime, score,platform)" +
                " values (?,?,?,?,?,?,?)");
        int batchSize = 1;
        int current_size = 0;
        while (res.next()) {
            if (current_size == batchSize) {
                current_size = 0;
                pstmt.executeBatch();
                pstmt.clearBatch();
            }
            for (int i = 1; i < 8; i++) {
                if (i == 7) {
                    pstmt.setString(i, "haodaxue");
                    continue;
                }
                if (res.getString(i) == null) {
                    if (i == 1 || i == 4 || i == 6)
                        pstmt.setString(i, "0");
                    else if (i == 5)
                        pstmt.setString(i, "2000-01-01 00:00:00");
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

    public void test_import() throws SQLException {
        Statement stmt_source = conn_source.createStatement();
        Statement stmt_dest = conn_dest.createStatement();
        String sql = "create table IF NOT EXISTS test_basic (" +
                "test_id  varchar(50) character set gbk,"
                + "test_name varchar(50) character set gbk,"
                + "course_open_id varchar(50) character set gbk,"
                + "course_name varchar(50) character set gbk,"
                + "course_open_name varchar(50) character set gbk,"
                + "beginTime datetime,"
                + "endTime datetime,"
                + "type int(11),"
                + "platform varchar(50) character set gbk )";
        stmt_dest.execute(sql);
        ResultSet res;
        sql = "select test_paper_id,test_name, mooc_open_unit_item.course_open_id, course_name, opencourse_name, begin_time,end_time,test_paper_type from core_test_content "
                + "left join core_test on core_test.test_id = core_test_content.test_id "
                + "left join mooc_open_unit_item on core_test_content.test_paper_id = mooc_open_unit_item.relative_id "
                + "left join mooc_course on mooc_course.course_id = mooc_open_unit_item.course_id "
                + "left join mooc_open_course on mooc_open_course.course_open_id = mooc_open_unit_item.course_open_id "
                + "where (mooc_open_course.publish_flag=50 or mooc_open_course.publish_flag=60) and (mooc_open_unit_item.item_type=50 or mooc_open_unit_item.item_type=60)";
        res = stmt_source.executeQuery(sql);
        PreparedStatement pstmt = conn_dest.prepareStatement("insert into test_basic (test_id,test_name,course_open_id,course_name, course_open_name,beginTime,endTime, type,platform)" +
                " values (?,?,?,?,?,?,?,?,?)");
        int batchSize = 1;
        int current_size = 0;
        while (res.next()) {
            if (current_size == batchSize) {
                current_size = 0;
                pstmt.executeBatch();
                pstmt.clearBatch();
            }
            for (int i = 1; i < 10; i++) {
                if (i == 9) {
                    pstmt.setString(i, "haodaxue");
                    continue;
                }
                if (res.getString(i) == null) {
                    if (i == 8)
                        pstmt.setString(i, "0");
                    else if (i == 7 || i == 6)
                        pstmt.setString(i, "2000-01-01 00:00:00");
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
        TestAdapter adp = new TestAdapter();
        adp.test_import();
    }
}
