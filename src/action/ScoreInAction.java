package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class ScoreInAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
	//ローカル変数の宣言 1
			String url = "";
	//フォワード
			url = "scoreIn.jsp";
			req.getRequestDispatcher(url).forward(req, res);
	}
}
