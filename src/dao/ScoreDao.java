package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Score;
import bean.Student;
import bean.Subject;

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
		List<Score> list =  new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("select * from STUDENT AS st,GRADE_CLASS AS gc,SCORE AS sc,SUBJECT AS su "
														+ "where sc.STUDENT_ID = st.STUDENT_ID AND st.GRADE_CLASS_ID = gc.GRADE_CLASS_ID AND sc.SUBJECT_ID=su.SUBJECT_ID "
														+ "AND st.GRADE_CLASS_ID=? AND gc.GRADE_CLASS_ID=? ORDER BY sc.STUDENT_ID");
			// プリペアードステートメントに教員IDをバインド
			statement.setInt(1, class_id);
			statement.setInt(2, subject_id);
			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			  // リザルトセットを全件走査
	        while (rSet.next()) {
	            // 新しいGradeClassインスタンスを生成し、検索結果をセット
	        	Score scores = new Score();
	        	Student student = new Student();
	        	Subject subject = new Subject();
	        	scores.setScoreId(rSet.getInt("SCORE_ID"));//成績ID
	        	scores.setStudentId(rSet.getInt("STUDENT_ID"));//学生番号
	        	scores.setSubjectCode(rSet.getString("SUBJECT_CODE"));//科目コード
	        	scores.setScoreMonth(rSet.getInt("SCORE_MONTH"));//成績月
	        	scores.setScoreValue(rSet.getInt("SCORE_VALUE"));//成績点数
	        	// 学生名をセット
	        	student.setStudentName(rSet.getString("STUDENT_NAME"));//学生名
	        	scores.setStudent(student);
	        	// 科目目をセット
	        	subject.setSubjectName(rSet.getString("SUBJECT_NAME"));//科目名
	        	subject.setSubjectId(rSet.getInt("SUBJECT_ID"));//科目ID
	        	scores.setSubject(subject);

	            // リストに追加
	            list.add(scores);
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
