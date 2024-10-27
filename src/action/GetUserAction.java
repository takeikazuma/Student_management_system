package action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Users;
import dao.UsersDao;
import tool.Action;


public class GetUserAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		//ローカル変数の宣言 1
		String url = "";
		String id = "";
		String password = "";
		UsersDao usersDao = new UsersDao();
		Users users = null;


//シーケンスNo.1：メソッドNo.8(ユーザ情報を取得)
		//リクエストパラメータ―の取得 2
		id = req.getParameter("id");// 教員ID
		password = req.getParameter("password");//パスワード

		//DBからデータ取得 3
		users = usersDao.getUser(id, password);//ユーザデータ取得

		if (users != null) {// 認証成功の場合
			// セッション情報を取得
			HttpSession session = req.getSession(true);
//			// 認証済みフラグを立てる(必要か検討：落合)
			users.setAuthenticated(true);
			// セッションにログイン情報を保存
			session.setAttribute("users", users);

//シーケンスNo.2：メソッドNo.6(学生情報を取得)


//シーケンスNo.3：メソッドNo.2(出席情報を取得)

//			リダイレクト
//			url = "menu.jsp";
//			res.sendRedirect(url);
			req.getRequestDispatcher("menu.jsp").forward(req, res);
		} else {
			// 認証失敗の場合
			// エラーメッセージをセット
			List<String> errors = new ArrayList<>();
			errors.add("IDまたはパスワードが確認できませんでした");
			req.setAttribute("errors", errors);
			// 入力されたユーザIDをセット
			req.setAttribute("id", id);

			//フォワード
			url = "login.jsp";
			req.getRequestDispatcher(url).forward(req, res);
		}
	}
}
