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


public class SubjectScoreOutAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//	//ローカル変数の宣言 1
		String url = "";
		String class_id_str = "";
		String subject_id_str = "";
		String courseYear_str = "";
		String subject_name ="";
		String subject_code ="";
		String registercheck_str ="";
		int class_id = 0;
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
		class_id_str = req.getParameter("class_id");// クラスID
		subject_id_str = req.getParameter("subject_id");//科目ID
		courseYear_str = req.getParameter("courseYear");//履修学年

//		subject_name = req.getParameter("class_name");//クラス名
//		subject_code = req.getParameter("subject_code");//科目コード

		//クラスIDが送信されていた場合
		if (class_id_str != null ) {
			// 数値に変換
			class_id = Integer.parseInt(class_id_str);
		}
		//科目IDが送信されていた場合
		if (subject_id_str != null) {
			// 数値に変換
			subject_id = Integer.parseInt(subject_id_str);
		}
		//履修学年が送信されていた場合
		if (courseYear_str != null) {
			// 数値に変換
			courseYear = Integer.parseInt(courseYear_str);
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
		if (class_id  != 0 && subject_id != 0 && courseYear != 0) {
			score_list = scoreDao.getScore(class_id,subject_id,courseYear);//成績データ取得
			subject = subjectDao.getSubjectNameCode(subject_id);//科目名、科目コード取得
		    subject_name = subject.getSubjectName();
		    subject_code = subject.getSubjectCode();
		}

		// 各学生の月を一括更新用
		String bulkMonthStr = req.getParameter("bulk_month");
		if (bulkMonthStr != null && !bulkMonthStr.isEmpty()) {
		    int bulkMonth = Integer.parseInt(bulkMonthStr);

		    // 各学生の月を一括で更新
		    for (Score score : score_list) {
		        score.setScoreMonth(bulkMonth); // 一括で設定された月を適用
		    }
		}


//レスポンス値をセット6
		// リクエストにクラス一覧をセット
		req.setAttribute("class_list", class_list);
		// リクエストに科目一覧をセット
		req.setAttribute("subject_list", subject_list);
		// リクエストに成績一覧をセット
		req.setAttribute("score_list", score_list);

		req.setAttribute("class_id", class_id);
		req.setAttribute("subject_id",subject_id);
		req.setAttribute("courseYear", courseYear);
		req.setAttribute("subject_name", subject_name);
		req.setAttribute("subject_code", subject_code);

	//フォワード
		url = "subjectScoreOut.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
