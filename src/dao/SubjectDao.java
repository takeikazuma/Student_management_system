package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Subject;

public class SubjectDao extends DAO {
	/**
	 * filterメソッド 学校を指定して科目の一覧を取得する
	 *
	 * @param school:School
	 *            学校
	 * @return 科目のリスト:List<Subject> 存在しない場合は0件のリスト
	 * @throws Exception
	 */
	public List<Subject> getSubject() throws Exception {
		// リストを初期化
		List<Subject> list = new ArrayList<>();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントに値をセット
			statement = connection.prepareStatement("select * from SUBJECT");
			ResultSet rSet = statement.executeQuery();

			// リザルトセットを全件走査
			while (rSet.next()) {
				// 科目インスタンスの初期化
				Subject subject = new Subject();
				// 科目インスタンスに値をセット
				subject.setSubjectId(rSet.getInt("SUBJECT_ID"));
				subject.setSubjectCode(rSet.getString("SUBJECT_CODE"));
				subject.setSubjectName(rSet.getString("SUBJECT_NAME"));
				subject.setCourseYear(rSet.getInt("COURSE_YEAR"));
				// リストに追加
				list.add(subject);
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
