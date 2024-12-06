package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GradeClassDao extends DAO {
	/***
	 * キー:クラスID、値:クラス名のクラスマップを取得
	 *
	 * @return クラスマップ:HashMap<クラスID:Integer, クラス名:String>
	 * @throws Exception
	 */
	public HashMap<Integer, String> getClassMap() throws Exception {
		// リストを初期化
		HashMap<Integer, String> classMap = new HashMap<>();

		// データベースへのコネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(
					"select GRADE_CLASS_ID, GRADE_CLASS_NAME from GRADE_CLASS order by GRADE_CLASS_ID");

			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			// リザルトセットを全件走査
			while (rSet.next()) {
				// 新しいGradeClassインスタンスを生成し、検索結果をセット
				classMap.put(rSet.getInt("GRADE_CLASS_ID"), rSet.getString("GRADE_CLASS_NAME"));
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
		return classMap;
	}
}
