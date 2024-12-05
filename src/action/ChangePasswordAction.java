package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Users;
import dao.UsersDao;
import tool.Action;


public class ChangePasswordAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ローカル変数の宣言
		String nowPassword = "";
		String newPassword = "";
		UsersDao usersDao = new UsersDao();
		Users users = null;
		boolean update = false;

		// リクエストパラメータの取得
		nowPassword = req.getParameter("nowPassword");// 現在のパスワード
		newPassword = req.getParameter("newPassword");// 新しいパスワード

		// セッション情報を取得
		HttpSession session = req.getSession(true);
		// セッションからログイン情報を取得
		Object sessionUser = session.getAttribute("users");
		// セッション情報をUsers型にキャスト
		users = (Users)sessionUser;

		// ユーザの存在チェック
		users = usersDao.getUser(Integer.toString(users.getUsersId()), nowPassword);

		if (users != null) { // 現在のパスワードが正しい場合

			// パスワード更新
			update = usersDao.updateUser(users.getUsersId(), newPassword);

			if (update) {
				// パスワードの更新に成功した場合
				req.setAttribute("message", "パスワードを更新しました。");
			} else {
				// パスワードの更新に失敗した場合
				req.setAttribute("message", "パスワードの更新に失敗しました。");
			}
		} else {
			// データが取得されなかった場合
			req.setAttribute("message", "現在のパスワードが間違っています。");
		}

		//フォワード
		req.getRequestDispatcher("changePassword.jsp").forward(req, res);
	}
}
