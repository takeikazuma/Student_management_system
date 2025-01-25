package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Score;
import bean.Student;
import dao.ScoreDao;
import dao.StudentDao;
import tool.Action;


public class StudentScoreAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//	//ローカル変数の宣言 1
		String url = "";
		String student_id_str = "";
		String registercheck_str ="";
		int student_id = 0;
		StudentDao studentDao = new StudentDao();
		ScoreDao scoreDao = new ScoreDao();
		List<Score> score_list = null;
		List<Student> student_info = null;

//	//リクエストパラメータ―の取得 2
		student_id_str = req.getParameter("student_id");// 学生番号

		//学生番号が送信されていた場合
		if (student_id_str != null ) {
			// 数値に変換
			student_id = Integer.parseInt(student_id_str);
		}

		registercheck_str = req.getParameter("registercheck_flag");//成績登録後かチェック用
		if (registercheck_str != null) {
			// 変換
			boolean registercheck = false;
			registercheck = Boolean.parseBoolean(registercheck_str);
			req.setAttribute("registercheck_flag",registercheck);
		}
//シーケンスNo.21：メソッドNo.3(プルダウンに表示するクラスの一覧を取得)


//シーケンスNo.23：メソッドNo.5(学生番号に一致する成績情報の取得)

		//DBからデータ取得 3
		if (student_id != 0 && (student_id >= 1_000_000 && student_id <= 9_999_999)) {
			student_info = studentDao.getStudent(student_id);
			if(student_info!=null){
				score_list = scoreDao.getScore(student_id,"in");//成績データ取得
			}
		}

//レスポンス値をセット6
		// リクエストに成績一覧をセット
		req.setAttribute("score_list", score_list);
		req.setAttribute("student_info", student_info);
		if (student_id == 0) {
	        req.setAttribute("student_id", "");  // student_id を空の文字列に設定
	    } else {
	        req.setAttribute("student_id", student_id);  // 通常のstudent_idをセット
	    }
	//フォワード
		url = "studentScore.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
