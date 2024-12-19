package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bean.Score;
import bean.Student;
import bean.Subject;

public class ScoreDao extends DAO {

	/**
	 * クラス、科目に一致する成績情報の取得
	 *
	 * @param class_id:int,
	 *            クラスID
	 * @param subject_id:int
	 *            学生番号
	 * @return 認証成功:Scoreクラスのインスタンス, 認証失敗:null
	 * @throws Exception
	 */
	public List<Score> getScore(int class_id,int subject_id,int courseYear) throws Exception {
		// Usersインスタンスを初期化
		List<Score> list =  new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM STUDENT AS st "
													+ "LEFT JOIN "
													+ "(SELECT * FROM SCORE AS sc,SUBJECT AS su "
													+ "WHERE sc.subject_id = su.subject_id AND sc.subject_id = ?) AS sc_su "
													+ "ON st.student_id = sc_su.student_id "
													+ "WHERE st.grade_class_id = ? AND st.school_year = ? ORDER BY st.student_kana");
			// プリペアードステートメントに教員IDをバインド
			statement.setInt(1, subject_id);
			statement.setInt(2, class_id);
			statement.setInt(3, courseYear);
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

	/**
	 * 1件の成績を更新
	 *
	 * @param student_id:String
	 *            学生番号
	 * @param subject_code:String
	 *            科目コード
	 * @param month:int
	 *            入力月
	 * @param score:int
	 *            得点
	 * @return 登録成功:true, 認証失敗:null
	 * @throws Exception
	 */
	public boolean saveScore(String subject_code,List<Score> score_list) throws Exception {

		// コネクションを確立
		Connection connection = getConnection();

		// コミット許可フラグ
		boolean canCommit = true;

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		try {
			// 自動コミットをオフ
			connection.setAutoCommit(false);
			// リストを全件走査
			for (Score score : score_list) {
				// 1件ずつ保存
				PreparedStatement statement = null;
				// 実行件数
				int count = 0;

				try {
					// データベースから成績を取得
//					System.out.println(score);
					Score old = getScoreId(score.getStudentId(),score.getSubject().getSubjectId());
//					System.out.println(old);
					if (old == null) {
						// 成績が存在しなかった場合
						// プリペアードステートメントにINSERT文をセット
						statement = connection.prepareStatement(
								"INSERT INTO score(student_id,subject_id,score_month,score_value,reg_date,update_date) values(?,?,?,?,?,?)");
						statement.setInt(1, score.getStudentId());
						statement.setInt(2, score.getSubject().getSubjectId());
						statement.setInt(3, score.getScoreMonth());
						statement.setInt(4, score.getScoreValue());
						statement.setTimestamp(5, currentTimestamp); // reg_date
					    statement.setTimestamp(6, currentTimestamp); // update_date
//					    System.out.println(score.getStudentId());
//					    System.out.println(score.getSubject().getSubjectId());
//					    System.out.println(score.getScoreMonth());
//					    System.out.println(score.getScoreValue());
//					    System.out.println(currentTimestamp);
					} else {
						// 成績が存在する場合
						// プリペアードステートメントにUPDATE文をセット
						statement = connection.prepareStatement(
								"UPDATE score SET score_month=?,score_value=?,update_date=? where score_id=?");
						statement.setInt(1, score.getScoreMonth());
						statement.setInt(2, score.getScoreValue());
						statement.setTimestamp(3,currentTimestamp);//update_date
						statement.setInt(4,old.getScoreId());
					}
					// プリペアードステートメントを実行
					count = statement.executeUpdate();
					System.out.println(count);
					} catch (Exception e) {
						throw e;
					} finally {
						if (statement != null) {
							try {
								statement.close();
							} catch (SQLException sqle) {
								throw sqle;
							}
						}
					}

					if (count > 0) {
						canCommit = true;
					} else {
						// 失敗の場合
						// ループを抜ける
						canCommit = false;
						break;
					}
			}
				if (canCommit) {
					// 全て正常終了の場合
					// コミット
					connection.commit();
				} else {
					throw new Exception();
				}
			} catch (SQLException sqle) {
				// エラーが発生した場合
				try {
					// ロールバック
					connection.rollback();
				} catch (SQLException e) {
					throw e;
				}

			} finally {
				if (connection != null) {
					try {
						connection.setAutoCommit(true);
						connection.close();
					} catch (SQLException sqle) {
						throw sqle;
					}
				}
			}
			return canCommit;
		}
	/**
	 * 学生番号と科目IDが一致する成績データを1件取得する
	 *
	 * @param student_id:int
	 *            学生番号
	 * @param subject_id:int
	 *            科目ID
	 * @return 対象あり:Scoreクラスのインスタンス, 対象なし:null
	 * @throws Exception
	 */
	public Score getScoreId(int student_id,int subject_id) throws Exception {
		// Usersインスタンスを初期化
		Score score =  new Score();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM score WHERE student_id = ? AND subject_id = ?");
			// プリペアードステートメントに教員IDをバインド
			statement.setInt(1, student_id);
			statement.setInt(2, subject_id);
			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();
			if (rSet.next()) {
				// リザルトセットが存在する場合
				// scoreインスタンスに検索結果をセット
				score.setScoreId(rSet.getInt("score_id"));
			} else {
				// リザルトセットが存在しない場合
				// Usersインスタンスにnullをセット
				score = null;
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
		return score;
	}
}
