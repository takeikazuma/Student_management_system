package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Users;

public class UsersDao extends DAO {

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
	public Users getUser(String id, String password) throws Exception {
		// Usersインスタンスを初期化
		Users users = new Users();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(
					"SELECT * "
					+ "FROM users INNER JOIN grade_class "
					+ "ON users.grade_class_id = grade_class.grade_class_id "
					+ "WHERE users_id=?");
			// プリペアードステートメントに教員IDをバインド
			statement.setInt(1, Integer.parseInt(id));
			// プリペアードステートメントを実行
			ResultSet rSet = statement.executeQuery();

			if (rSet.next()) {
				// リザルトセットが存在する場合
				// Usersインスタンスに検索結果をセット
				users.setUsersId(rSet.getInt("users_id"));
				users.setPassword(rSet.getString("password"));
				users.setUsersName(rSet.getString("users_name"));
				users.setGradeClassId(rSet.getInt("grade_class_id"));
				users.setGradeClassName(rSet.getString("grade_class_name"));
			} else {
				// リザルトセットが存在しない場合
				// Usersインスタンスにnullをセット
				users = null;
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
		// ユーザがnullまたはパスワードが一致しない場合
		//Userテーブルのpasswordはデータ型をcharacter varying(vercharのこと)に変更が必要(落合)
		if (users == null || !users.getPassword().equals(password)) {
			return null;
		}
		return users;
	}

	/**
	 * updateUserメソッド パスワードを更新する
	 *
	 * @param user_id:Int
	 *            ユーザID
	 * @param newPassword:String
	 *            パスワード
	 * @return 更新成功:true, 更新失敗:false
	 * @throws Exception
	 */
	public boolean updateUser(int user_id, String newPassword) throws Exception {
		boolean result = false;
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement(
					"UPDATE users "
					+ "SET password = '" + newPassword + "' "
					+ "WHERE users_id = " + user_id );
			// プリペアードステートメントを実行
			int rSet = statement.executeUpdate();

			if (rSet >= 1) {
				result = true;
			} else {
				result = false;
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
		return result;
	}

}
