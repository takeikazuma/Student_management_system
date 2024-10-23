package dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;
/**
 * <p>Dao</p>
 * <p>全てのSQLはこちらのDAOを継承して作成する</p>
 *
 * @author 東京情報校　竹井　一馬
 * @version 1.0
 */
public class DAO {
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
