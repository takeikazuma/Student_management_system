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
	 * クラス、科目、入学年度に一致する成績情報の取得
	 *
	 * @param class_id:int,
	 *            クラスID
	 * @param subject_id:int
	 *            科目ID
	 * @param courseYear:int
	 *            入学年度
	 * @param sql_select:String
	 *            inまたはout 入力用か出力用か判断
	 * @return 成功:Scoreクラスのインスタンス, 失敗:null
	 * @throws Exception
	 */
	public List<Score> getScore(int class_id,int subject_id,int courseYear,String sql_select) throws Exception {
		// Usersインスタンスを初期化
		List<Score> list =  new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			if(sql_select.equals("in")){
//未入力成績科目も出す場合
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM STUDENT AS st "
													+ "LEFT JOIN "
													+ "(SELECT * FROM SCORE AS sc,SUBJECT AS su "
													+ "WHERE sc.subject_id = su.subject_id AND sc.subject_id = ?) AS sc_su "
													+ "ON st.student_id = sc_su.student_id "
													+ "LEFT JOIN grade_class AS gc ON st.grade_class_id = gc.grade_class_id "
													+ "WHERE st.grade_class_id = ? AND st.admission_year = ? ORDER BY st.student_kana");
			}else if(sql_select.equals("out")){
//登録済みのみ成績科目を出す場合
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM STUDENT AS st "
													+ "LEFT JOIN (SELECT * FROM SCORE AS sc,SUBJECT AS su "
													+ "WHERE sc.subject_id = su.subject_id AND sc.subject_id = ?) AS sc_su "
													+ "ON st.student_id = sc_su.student_id "
													+ "LEFT JOIN grade_class AS gc ON st.grade_class_id = gc.grade_class_id "
													+ "WHERE st.grade_class_id = ? AND st.admission_year = ? AND sc_su.score_month <> 0 "
													+ "ORDER BY st.student_kana");
			}
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
	        	scores.setSubjectId(rSet.getInt("SUBJECT_ID"));//科目コード
	        	scores.setScoreMonth(rSet.getInt("SCORE_MONTH"));//成績月
	        	scores.setScoreValue(rSet.getInt("SCORE_VALUE"));//成績点数
	        	// 学生名をセット
	        	student.setStudentName(rSet.getString("STUDENT_NAME"));//学生名
	        	student.setAdmissionYear(rSet.getInt("ADMISSION_YEAR"));//入学年度
	        	student.setSchoolYear(rSet.getInt("SCHOOL_YEAR"));//学年
	        	student.setGradeClassName(rSet.getString("GRADE_CLASS_NAME"));//クラス名
	        	scores.setStudent(student);
	        	// 科目目をセット
	        	subject.setSubjectName(rSet.getString("SUBJECT_NAME"));//科目名
	        	subject.setSubjectCode(rSet.getString("SUBJECT_CODE"));//科目コード
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
	 * 学生番号に一致する成績情報の取得
	 *
	 * @param student_id:int,
	 *            学生番号
	 * @param sql_select:String,
	 *            inまたはout 入力用か出力用か判断
	 * @return 成功:対象学生に成績データ, 失敗:
	 * @throws Exception
	 */
	public List<Score> getScore(int student_id,String sql_select) throws Exception {
		// Usersインスタンスを初期化
		List<Score> list =  new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット

			if(sql_select.equals("in")){
//未入力成績科目も出す場合
				statement = connection.prepareStatement("SELECT * FROM subject AS su "
															+ "LEFT JOIN score AS sc ON su.subject_id = sc.subject_id AND sc.student_id = ? "
															+ "LEFT JOIN student AS st ON sc.student_id = st.student_id "
															+ "LEFT JOIN grade_class AS gc ON st.grade_class_id = gc.grade_class_id "
															+ "ORDER BY su.course_year,su.subject_id;");
				}else if(sql_select.equals("out")){
//登録済みのみ成績科目を出す場合
				statement = connection.prepareStatement("SELECT su.subject_id,su.subject_code,su.subject_name,su.course_year, "
															+ "sc.score_id,sc.student_id,sc.score_month,sc.score_value, "
															+ "st.admission_year,st.student_name,st.school_year,st.is_enrollment, "
															+ "gc.grade_class_id,gc.grade_class_name "
															+ "FROM subject AS su,score AS sc,student AS st,grade_class AS gc "
															+ "WHERE su.subject_id = sc.subject_id AND sc.student_id = st.student_id "
															+ "AND st.grade_class_id = gc.grade_class_id AND sc.student_id = ? "
															+ "AND sc.score_month <> 0 ORDER BY su.course_year,su.subject_id;");}
			// プリペアードステートメントに教員IDをバインド
			statement.setInt(1, student_id);
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
	        	scores.setSubjectId(rSet.getInt("SUBJECT_ID"));//科目ID
	        	scores.setScoreMonth(rSet.getInt("SCORE_MONTH"));//成績月
	        	scores.setScoreValue(rSet.getInt("SCORE_VALUE"));//成績点数
	        	// 学生情報をセット
	        	student.setStudentName(rSet.getString("STUDENT_NAME"));//学生名
	        	student.setAdmissionYear(rSet.getInt("ADMISSION_YEAR"));//入学年度
	        	student.setSchoolYear(rSet.getInt("SCHOOL_YEAR"));//学年
	        	student.setIsEnrollment(rSet.getBoolean("IS_ENROLLMENT"));//在学退学フラグ
	        	student.setGradeClassId(rSet.getInt("GRADE_CLASS_ID"));//クラスID
	        	student.setGradeClassName(rSet.getString("GRADE_CLASS_NAME"));//クラス名
	        	scores.setStudent(student);
	        	// 科目情報をセット
	        	subject.setSubjectName(rSet.getString("SUBJECT_NAME"));//科目名
	        	subject.setSubjectCode(rSet.getString("SUBJECT_CODE"));//科目コード
	        	subject.setCourseYear(rSet.getInt("COURSE_YEAR"));//学年
	        	scores.setSubject(subject);

	        	//データ確認用
//	        	System.out.println(rSet.getInt("SCORE_ID"));
//	        	System.out.println(rSet.getInt("STUDENT_ID"));
//	        	System.out.println(rSet.getString("SUBJECT_CODE"));
//	        	System.out.println(rSet.getInt("SCORE_MONTH"));
//	        	System.out.println(rSet.getInt("SCORE_VALUE"));
//	        	System.out.println(rSet.getString("STUDENT_NAME"));
//	        	System.out.println(rSet.getString("SUBJECT_NAME"));
//	        	System.out.println(rSet.getInt("SUBJECT_ID"));
//	        	System.out.println(rSet.getInt("COURSE_YEAR"));
//	        	System.out.println();

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
					Score old = getScoreId(score.getStudentId(),score.getSubjectId());
//					System.out.println(old);
					if (old == null) {
						// 成績が存在しなかった場合
						// プリペアードステートメントにINSERT文をセット
						statement = connection.prepareStatement(
								"INSERT INTO score(student_id,subject_id,score_month,score_value,reg_date,update_date) values(?,?,?,?,?,?)");
						statement.setInt(1, score.getStudentId());
						statement.setInt(2, score.getSubjectId());
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
	 * 1件の成績を更新
	 *
	 * @param score_list:List<Score>
	 *            成績リスト
	 * @return 登録成功:true, 認証失敗:null
	 * @throws Exception
	 */
	public boolean saveScore(List<Score> score_list) throws Exception {
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
					System.out.println(score);
					Score old = getScoreId(score.getStudentId(),score.getSubjectId());
//					System.out.println(old);
					if (old == null) {
						// 成績が存在しなかった場合
						// プリペアードステートメントにINSERT文をセット
						statement = connection.prepareStatement(
								"INSERT INTO score(student_id,subject_id,score_month,score_value,reg_date,update_date) values(?,?,?,?,?,?)");
						statement.setInt(1, score.getStudentId());
						statement.setInt(2, score.getSubjectId());
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
