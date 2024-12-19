package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SelectAttendAction extends AllAttendRegistAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		List<String> results = new ArrayList<>();
		List<String> errors = new ArrayList<>();

		String url = "";
		int admissionYear = 0;
		int year = 0;
		Integer month = 0;
		Integer day = 0;
		String className = "";

		LinkedHashMap<Integer, HashMap<String, String>> studentFieldsMap = new LinkedHashMap<>();	// 学生の順番が重要なため、LinkedHashMapを使用(HashMapだと順番が入れ替わる場合がある

		//リクエストパラメータ―の取得 2
		try {	// int化できなければ0
			admissionYear = Integer.parseInt(req.getParameter("admission_year"));	// 入学年度
		} catch (Exception e) {
			admissionYear = 0;
		}
		try {	// int化できなければ0
			year = Integer.parseInt(req.getParameter("year"));	// 編集年
		} catch (Exception e) {
			year = 0;
		}
		try {	// int化できなければ0
			month = Integer.parseInt(req.getParameter("month"));	// 編集月
		} catch (Exception e) {
			month = null;
		}
		try {	// int化できなければ0
			day = Integer.parseInt(req.getParameter("day"));	// 編集日
		} catch (Exception e) {
			day = null;
		}
		className = req.getParameter("class_name");	// クラス名

		// パラメーターが入力されている場合、一覧を取得
		if(admissionYear > 0 && className != null && year > 0 && month != null && day != null) {
			if(this.isValidDate(year, month, day)) {	// 有効な年月日か検証
				/** 学生出席状況を取得 **/
				studentFieldsMap = this.getStudentFieldsMap(admissionYear, className, year, month, day);
			}
			else {
				errors.add("年月日が正しくありません");
			}
		}

		// 出席状況を更新後ページ遷移してきた場合、メッセージ表示
		if(req.getRequestURL().toString().endsWith("RegistrationAttend.action")) {
			results.add("出席状況を更新しました");
		}

		// 検索結果をセット
		req.setAttribute("studentFieldsMap", studentFieldsMap);
		//req.setAttribute("attendMap", attendMap);

		// 入力された検索項目をセット
		req.setAttribute("admission_year", req.getParameter("admission_year"));
		req.setAttribute("class_name", req.getParameter("class_name"));
		req.setAttribute("year", req.getParameter("year"));
		req.setAttribute("month", req.getParameter("month"));
		req.setAttribute("day", req.getParameter("day"));

		req.setAttribute("errors", errors);
		req.setAttribute("results", results);

		//フォワード
		url = "input_attend.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
