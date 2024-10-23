package dao;

import java.sql.Connection;

public class UsersDAO extends DAO {
	public void getUser()throws Exception {
		Connection con=getConnection();
		con.setAutoCommit(false);

		con.commit();
		con.setAutoCommit(true);
		con.close();
		return ;
	}
}
