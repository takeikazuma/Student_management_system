package action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Attend;
import bean.Student;
import dao.AttendDao;
import dao.GradeClassDao;
import dao.StudentDao;
import tool.Action;

public class SelectAttendAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		String url = "";
		int admissionYear = 0;
		int year = 0;
		Integer month = 0;
		Integer day = 0;
		String className = "";
		StudentDao studentDao = new StudentDao();
		List<Student> studentList = null;
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


		// クラス マップを取得
		GradeClassDao gradeClassDao = new GradeClassDao();
		HashMap<Integer, String> classMap = gradeClassDao.getClassMap();

		//DBからデータ取得 3
		studentList = studentDao.getStudent(admissionYear, className);	// 学生データ取得

		for(Student student: studentList) {
			HashMap<String, String> studentFields = new HashMap<String, String>();

			studentFields.put("student_id",		String.valueOf(student.getStudentId()));	// 学生番号
//			studentFields.put("grade_class_id",	String.valueOf(student.getGradeClassId()));	// クラスID
			studentFields.put("grade_class_name",	classMap.get(student.getGradeClassId()));	// クラス名
			studentFields.put("admission_year",	String.valueOf(student.getAdmissionYear()));	// 入学年度
			studentFields.put("student_name",		String.valueOf(student.getStudentName()));	// 学生氏名
//			studentFields.put("student_kana",		String.valueOf(student.getStudentKana()));	// 学生氏名カナ
			studentFields.put("school_year",		String.valueOf(student.getSchoolYear()));	// 学年
//			studentFields.put("withdrawal_date",	String.valueOf(student.getWithdrawalDate()));	// 退学日付
//			studentFields.put("is_enrollment",		String.valueOf(student.getIsEnrollment()));		// 在学退学フラグ
			studentFieldsMap.put(student.getStudentId(), studentFields);
		}

		// 検索結果をセット
		req.setAttribute("studentFieldsMap", studentFieldsMap);

		// 出席状況マップを作成
		HashMap<Integer, HashMap<Integer, Integer>> attendMap = new HashMap<>();	// HashMap:出席状況<Integer:学生番号, HashMap<Integer:出欠日, Integer:出欠状況>>

		AttendDao attendDao = new AttendDao();
		List<Attend> attendList = attendDao.getAttend(year, month, null, studentList);

		// 指定学生分の出席状況一覧(出席状況は空)を作成しておく
		for(Student student: studentList) {
			attendMap.put(student.getStudentId(), new HashMap<>());
		}
		for(Attend attend: attendList) {
			attendMap.get(Integer.valueOf(attend.getStudentId())).put(
					attend.getAttendDate().toLocalDate().getDayOfMonth(),	// 出欠日
				    attend.getAttendStatus()	// 出欠状況
			);
		}
		req.setAttribute("attendMap", attendMap);

		// 入力された検索項目をセット
		req.setAttribute("admission_year", admissionYear);
		req.setAttribute("year", year);
		req.setAttribute("month", month);
		req.setAttribute("day", day);
		req.setAttribute("class_name", className);

		//フォワード
		url = "input_attend.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}
}
