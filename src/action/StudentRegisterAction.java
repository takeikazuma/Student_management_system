package action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Score;
import bean.Subject;
import dao.ScoreDao;
import dao.SubjectDao;
import tool.Action;


public class StudentRegisterAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		//ローカル変数の宣言 1
//		// 入力された入力月用マップ()
//		Map<String, String> inputMonth = new HashMap<>();
//		// 入力された得点用マップ()
//		Map<String, String> inputValue = new HashMap<>();
		String url = "";
//		String subject_code ="";
		String subject_id_in = "";
		ScoreDao scoreDao = new ScoreDao();
		List<Score> score_list = new ArrayList<>();
		SubjectDao subjectDao = new SubjectDao();
		Subject subject = new Subject();
		String[] scoreStudentId = null;
		String monthStr = null;// 入力月文字列
		String valueStr = null;	// 得点文字列
		int studentid = 0;	// 学生番号
		int subjectid = 0; // 科目ID
		int month = 0;// 得点
		int value = 0;// 入力月
		int class_id = 0;
		int subject_id =0;
		int courseYear =0;
		boolean register;
//		String subjectcode = "";// 科目コード

// リクエストパラメータ―の取得 2

		subject_id_in = req.getParameter("subject_id_in");//科目id
		String class_id_str = req.getParameter("class_id");// クラスID
		String subject_id_str = req.getParameter("subject_id");//科目ID
		String courseYear_str = req.getParameter("courseYear");//履修学年

//シーケンスNo.24：メソッドNo.12(1件の成績を更新)
		// 入力された学生番号の一覧を取得
		scoreStudentId = req.getParameterValues("score_studentId_set[]");
		// 科目IDを整数に変換
		subjectid = Integer.parseInt(subject_id_in);
		//科目コードを取得
		subject = subjectDao.getSubjectNameCode(subjectid);

		// 学生を全件走査
		for (String studentId : scoreStudentId) {
			// 成績インスタンスを初期化
			Score score = new Score();

			// 入力された「month_学生番号」の入力月文字列を取得
			monthStr = req.getParameter("month_" + studentId);

			// 入力された「value_学生番号」の得点文字列を取得
			valueStr = req.getParameter("value_" + studentId);

//			// 入力月用マップに学生番号と入力月文字列を格納
//			inputMonth.put(studentId, monthStr);

//			// 得点用マップに学生番号と得点文字列を格納
//			inputValue.put(studentId, valueStr);

			// 入力月文字列を整数に変換
			month = Integer.parseInt(monthStr);
			// 得点文字列を整数に変換
			value = Integer.parseInt(valueStr);
			// 学生番号を整数に変換
			studentid = Integer.parseInt(studentId);

			// 成績インスタンスに値をセット
			score.setStudentId(studentid);
			score.setScoreMonth(month);
			score.setScoreValue(value);
			Subject subject_a = new Subject();
			subject_a.setSubjectId(subjectid);
			score.setSubject(subject_a);
			score.setSubjectCode(subject.getSubjectCode());

			score_list.add(score);
		}
		register = scoreDao.saveScore(subject.getSubjectCode(),score_list);

		if(register){
			req.setAttribute("registercheck_flag", true);
		}
		else{
			req.setAttribute("registercheck_flag", false);
		}
//レスポンス値をセット6

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
		req.setAttribute("class_id", class_id);
		req.setAttribute("subject_id",subject_id);
		req.setAttribute("courseYear", courseYear);


//フォワード
		url = "ClassScore.action";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
