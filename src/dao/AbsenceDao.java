package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import bean.Absence;

public class AbsenceDao extends DAO {
    public void insertAbsence(Absence absence) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            // INSERT SQL文を修正：ABSENCE_IDは自動生成されるので指定しない
            String sql = "INSERT INTO ABSENCE (GRADE_CLASS_ID, START_DATE, END_DATE, REG_DATE, UPDATE_DATE) " +
                         "VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);

            // プレースホルダに値をセット
            statement.setInt(1, absence.getGradeClassId());  // クラスID
            statement.setDate(2, absence.getStartDate());    // 開始日
            statement.setDate(3, absence.getEndDate());      // 終了日
            statement.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));  // 登録日時
            statement.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));  // 更新日時

            // SQLを実行
            statement.executeUpdate();
        } catch (SQLException e) {
            // エラーメッセージを表示
            System.out.println("SQLエラー: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("データベースに挿入中にエラーが発生しました", e);
        } finally {
            // ステートメントとコネクションを閉じる
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}
