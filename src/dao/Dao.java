package dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Dao {
	/**
	 * データソース:DataSource:クラスフィールド
	 */
	static DataSource ds;

	/**
	 * getConnectionメソッド データベースへのコネクションを返す
	 *
	 * @return データベースへのコネクション:Connection
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		// データソースがnullの場合
		if (ds == null) {
			// InitialContextを初期化
			InitialContext ic = new InitialContext();
			// データベースへ接続
			ds = (DataSource) ic.lookup("java:/comp/env/jdbc/yajima");
		}
		// データベースへのコネクションを返却
		return ds.getConnection();
	}
}
