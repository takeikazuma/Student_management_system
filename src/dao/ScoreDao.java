package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import bean.Score;

public class ScoreDao extends DAO {

	/**
	 * loginメソッド ユーザIDとパスワードで認証する
	 *
	 * @param id:Int
	 *            ユーザID
	 * @param password:String
	 *            パスワード
	 * @return 認証成功:ユーザクラスのインスタンス, 認証失敗:null
	 * @throws Exception
	 */
	public List<Score> getScore(int class_id,int subject_id) throws Exception {
		// Usersインスタンスを初期化
		
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("select * from STUDENT AS st,GRADE_CLASS AS gc,SCORE AS sc "
												+ "	where sc.STUDENT_ID = st.STUDENT_ID AND st.GRADE_CLASS_ID = gc.GRADE_CLASS_ID "
												+ "AND GRADE_CLASS_ID=? AND SUBJECT_ID=? ORDER BY sc.STUDENT_ID");
			// プリペアードステートメントに教員IDをバインド
			statement.setInt(1, class_id);
			statement.setInt(2, subject_id);
			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			  // リザルトセットを全件走査
	        while (rSet.next()) {
	            // 新しいGradeClassインスタンスを生成し、検索結果をセット
	        	Score scores = new Score();
	            gradeclass.setGradeClassId(rSet.getInt("GRADE_CLASS_ID"));
	            gradeclass.setGradeClassName(rSet.getString("GRADE_CLASS_NAME"));

	            // リストに追加
	            list.add(gradeclass);
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
		return list;
	}
}