package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Absence;

public class AbsenceDao extends DAO {
	/**
	 * 長期休暇を取得
	 * @param gradeClassId クラスID (nullの場合は全データ取得)
	 * @param startDate		休暇開始日 (開始日・終了日がnullの場合は指定クラスIDの全データを取得)
	 * @param endDate		休暇終了日
	 * @return				長期休暇データ配列
	 * @throws Exception
	 */
	public List<Absence> getAbsence(Integer gradeClassId, Date startDate, Date endDate) throws Exception {
		// リストを初期化
	    List<Absence> list = new ArrayList<>();
		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

	    try {
	    	if(gradeClassId == null) {
	    		// クラスIDがnullの場合はすべて取得(クラスID順、長期欠席ID順)
		    	statement = connection.prepareStatement("select * from ABSENCE order by GRADE_CLASS_ID, ABSENCE_ID");
	    	}
	    	else if(startDate == null && endDate == null) {
	    		// 開始日・終了日共にnullの場合は指定クラスのデータをすべて取得(長期欠席ID順)
		    	statement = connection.prepareStatement("select * from ABSENCE where GRADE_CLASS_ID = ? order by ABSENCE_ID");
				statement.setInt(1, gradeClassId);
	    	}
	    	else {
	    		// 開始日・終了日が設定している場合は期間指定で取得
		    	statement = connection.prepareStatement("select * from ABSENCE where GRADE_CLASS_ID = ? and START_DATE <= ? and END_DATE >= ?");
				statement.setInt(1, gradeClassId);
				statement.setDate(2, endDate);
				statement.setDate(3, startDate);
	    	}

    		// プリペアードステートメントを実行
    		ResultSet rSet = statement.executeQuery();

    		// リザルトセットを全件走査
    		while (rSet.next()) {
    			// 新しいGradeClassインスタンスを生成し、検索結果をセット
    			Absence absence = new Absence();
    			absence.setAbsenceId(rSet.getInt("absence_id"));
    			absence.setGradeClassId(rSet.getInt("grade_class_id"));
    			absence.setStartDate(rSet.getDate("start_date"));
    			absence.setEndDate(rSet.getDate("end_date"));

    			// リストに追加
    			list.add(absence);
    		}

	    } catch (Exception e) {
			throw e;
		} finally {
			// プリペアードステートメントを閉じる
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			// コネクションを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
	    // データが無ければ、空のリストが返る
	    return list;
	}


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

