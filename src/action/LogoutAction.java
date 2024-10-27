package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tool.Action;

public class LogoutAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		HttpSession session = req.getSession();//セッション

		//リクエストパラメータ―の取得 2
		//なし
		//DBからデータ取得 3
		//なし

		//ビジネスロジック 4
		session.invalidate();//セッション全削除

		//DBへデータ保存 5
		//なし
		//レスポンス値をセット 6
		//なし

		//JSPへフォワード 7
		req.getRequestDispatcher("logout.jsp").forward(req, res);
	}
}
