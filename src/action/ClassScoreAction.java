package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.GradeClass;
import bean.Subject;
import dao.StudentDao;
import dao.SubjectDao;
import tool.Action;


public class ClassScoreAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//	//ローカル変数の宣言 1
		String url = "";
		String class_id_str = "";
		String subject_id_str = "";
		String courseYear_str = "";
		int class_id = 0;
		int subject_id =0;
		int courseYear =0;
		StudentDao studentDao = new StudentDao();
		List<GradeClass> class_list = null;
		SubjectDao subjectDao = new SubjectDao();
		List<Subject> subject_list = null;

//	//リクエストパラメータ―の取得 2
		class_id_str = req.getParameter("class_id");// クラスID
		subject_id_str = req.getParameter("subject_id");//科目ID
		courseYear_str = req.getParameter("courseYear");//履修学年

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

//シーケンスNo.21：メソッドNo.3(プルダウンに表示するクラスの一覧を取得)


		//DBからデータ取得 3
		class_list = studentDao.getClassList();//ユーザデータ取得

//シーケンスNo.22：メソッドNo.7(プルダウンに表示する科目の一覧を取得)

		//DBからデータ取得 3
		subject_list = subjectDao.getSubject();//ユーザデータ取得


//シーケンスNo.23：メソッドNo.5(クラス、科目に一致する成績情報の取得)
		if (class_id  != 0 && subject_id != 0 && courseYear != 0) {
//			getScore(int student_id,Int class_id,Int subject_id)
		}

//レスポンス値をセット6
		// リクエストにクラス一覧をセット
		req.setAttribute("class_list", class_list);
		// リクエストにクラス一覧をセット
		req.setAttribute("subject_list", subject_list);

		req.setAttribute("class_id", class_id);
		req.setAttribute("subject_id",subject_id);
		req.setAttribute("courseYear", courseYear);

	//フォワード
		url = "classScore.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
