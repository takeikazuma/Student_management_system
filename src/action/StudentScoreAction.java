package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.GradeClass;
import bean.Score;
import bean.Subject;
import dao.ScoreDao;
import dao.StudentDao;
import dao.SubjectDao;
import tool.Action;


public class StudentScoreAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//	//ローカル変数の宣言 1
		String url = "";
		String student_id_str = "";
		String subject_id_str = "";
		String courseYear_str = "";
		String subject_name ="";
		String subject_code ="";
		String registercheck_str ="";
		int student_id = 0;
		int subject_id =0;
		int courseYear =0;
		StudentDao studentDao = new StudentDao();
		List<GradeClass> class_list = null;
		SubjectDao subjectDao = new SubjectDao();
		List<Subject> subject_list = null;
		ScoreDao scoreDao = new ScoreDao();
		List<Score> score_list = null;
		Subject subject = new Subject();

//	//リクエストパラメータ―の取得 2
		student_id_str = req.getParameter("id");// 学生番号

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


		//DBからデータ取得 3
		class_list = studentDao.getClassList();//ユーザデータ取得

//シーケンスNo.22：メソッドNo.7(プルダウンに表示する科目の一覧を取得)

		//DBからデータ取得 3
		subject_list = subjectDao.getSubject();//ユーザデータ取得


//シーケンスNo.23：メソッドNo.5(クラス、科目に一致する成績情報の取得)

		//DBからデータ取得 3
		if (student_id != 0) {
			score_list = scoreDao.getScore(student_id);//成績データ取得
		}


//レスポンス値をセット6
		// リクエストに成績一覧をセット
		req.setAttribute("score_list", score_list);

		req.setAttribute("class_id", student_id);

	//フォワード
		url = "studentScore.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
