package action;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Absence;
import bean.Attend;
import bean.Student;
import dao.AbsenceDao;
import dao.AttendDao;
import dao.GradeClassDao;
import dao.StudentDao;
import tool.Action;

public class AllAttendRegistAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		List<String> results = new ArrayList<>();
		List<String> errors = new ArrayList<>();

		String url = "";
		int admissionYear = 0;
		int year = 0;
		int month = 0;
		String className = "";

		LinkedHashMap<Integer, HashMap<String, String>> studentFieldsMap = new LinkedHashMap<>();		// 学生の順番が重要なため、LinkedHashMapを使用(HashMapだと順番が入れ替わる場合がある

		//リクエストパラメータ―の取得 2
		try {	// int化できなければ0
			admissionYear = Integer.parseInt(req.getParameter("admission_year"));	// 入学年度
		} catch (Exception e) {
			admissionYear = 0;
		}
		try {	// int化できなければ0
			year = Integer.parseInt(req.getParameter("year"));	// 検索年
		} catch (Exception e) {
			year = 0;
		}
		try {	// int化できなければ0
			month = Integer.parseInt(req.getParameter("month"));	// 検索月
		} catch (Exception e) {
			month = 0;
		}
		className = req.getParameter("class_name");	// クラス名

		if(admissionYear > 0 && year > 0 && month > 0 && className.length() > 0) {	// パラメーターチェック
			if(this.isValidDate(year, month, 1)) {	// 有効な年月日か検証
				/** 学生出席状況を取得 **/
				studentFieldsMap = this.getStudentFieldsMap(admissionYear, className, year, month, null, true);
			}
			else {
				errors.add("年月が正しくありません");
			}
		}
		req.setAttribute("studentFieldsMap", studentFieldsMap);
		req.setAttribute("length_of_month", YearMonth.of(year, month).lengthOfMonth());

		// 入力された項目をセット
		req.setAttribute("admission_year", req.getParameter("admission_year"));
		req.setAttribute("class_name", req.getParameter("class_name"));
		req.setAttribute("year", req.getParameter("year"));
		req.setAttribute("month", req.getParameter("month"));

		req.setAttribute("errors", errors);
		req.setAttribute("results", results);

		//フォワード
		url = "all_attend.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}

	/**
	 * 年月日が正確か検証
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 正確な場合はtrue、不正確な場合はfalse
	 */
	protected boolean isValidDate(int year, int month, int day) {
		boolean result = false;
		try {
			// LocalDateを生成して検証
			LocalDate.of(year, month, 1);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 学生情報一覧を取得
	 * 他処理でも使用するので関数化
	 * @param admissionYear 入学年
	 * @param className クラス名
	 * @param year 抽出年
	 * @param month 抽出月
	 * @param day 抽出日 (ひと月分の場合はnull)
	 * @param useAttendSum 出席合計項目を付ける場合はtrue、必要ない場合はfalse
	 * @return 学生情報一覧:LinkedHashMap<学生ID:Integer, 学生情報項目:HashMap<項目名:String, 値:String>>
	 * @throws Exception
	 */
	protected LinkedHashMap<Integer, HashMap<String, String>> getStudentFieldsMap(int admissionYear, String className, int year, int month, Integer day, boolean useAttendSum) throws Exception {
		LinkedHashMap<Integer, HashMap<String, String>> studentFieldsMap = new LinkedHashMap<>();	// 学生情報一覧:LinkedHashMap<学生ID:Integer, 学生情報項目:HashMap<項目名:String, 値:String>>

		List<Student> studentList = null;	// 学生一覧
		HashMap<Integer, HashMap<Integer, Integer>> studentAttendMap = new HashMap<>();	// 出席状況一覧 HashMap<学生ID:Integer, 日別出席状況:HashMap<日:Integer, 出席状況:Integer>>
		HashMap<Integer, Double> studentAttendSumMap = new HashMap<>();	// 合計出席数 HashMap<学生ID:Integer, 合計出席数:Double>

		//DBからデータ取得 3
		StudentDao studentDao = new StudentDao();
		studentList = studentDao.getStudent(admissionYear, className);	// 学生データ取得

		// 学生の必要情報を配列に設定
		for(Student student: studentList) {
			HashMap<String, String> studentFields = new HashMap<String, String>();

			studentFields.put("student_id",			String.valueOf(student.getStudentId()));	// 学生番号
			studentFields.put("grade_class_name",	className);	// クラス名
			studentFields.put("admission_year",		String.valueOf(student.getAdmissionYear()));	// 入学年度
			studentFields.put("student_name",		String.valueOf(student.getStudentName()));	// 学生氏名
			studentFields.put("school_year",		String.valueOf(student.getSchoolYear()));	// 学年

			studentFieldsMap.put(student.getStudentId(), studentFields);

			// 学生別出欠を初期化
			studentAttendMap.put(student.getStudentId(), new HashMap<>());
			studentAttendSumMap.put(student.getStudentId(), 0.0);
		}

		// 出席状況マップを作成
		AttendDao attendDao = new AttendDao();
		List<Attend> attendList = attendDao.getAttend(year, month, null, studentList);
		for(Attend attend: attendList) {
			studentAttendMap.get(Integer.valueOf(attend.getStudentId())).put(
				attend.getAttendDate().toLocalDate().getDayOfMonth(),	// 出欠日
				attend.getAttendStatus());	//出欠状況コード
		}
		// 長期休暇は全学生 9 に設定
		int startDay;	// 抽出開始日
		int endDay;		// 抽出終了日
		if(day == null) {	// 抽出日がnullの場合、抽出月の1日から最終日に設定
			startDay = 1;
			endDay = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
		}
		else {
			startDay = day;
			endDay = day;
		}

		GradeClassDao gradeClassDao = new GradeClassDao();
		Integer gradeClassId = gradeClassDao.getClassId(className);	// クラス名からクラスIDを取得

		AbsenceDao absenceDao = new AbsenceDao();
		List<Absence> absenceList = absenceDao.getAbsence(			// 指定月の長期休暇を取得
				gradeClassId,
				Date.valueOf(LocalDate.of(year, month, startDay)),
				Date.valueOf(LocalDate.of(year, month, endDay)) );
		// 長期休暇があったら、全学生を9に設定
		for(Absence absence: absenceList) {		// 全件の長期休暇
			for(int i = absence.getStartDate().toLocalDate().getDayOfMonth(); i <= absence.getEndDate().toLocalDate().getDayOfMonth(); i++) {	// 開始日～終了日まで繰り返す
				for(Student student: studentList) {	// (指定クラスの)全学生
					studentAttendMap.get(student.getStudentId()).put(i, 9);	// 指定学生の出欠の値を 9
				}
			}
		}

		// 退学者の出席状況を 8 に設定
		for(Student student: studentList) {
			ZonedDateTime withdrawalDate = student.getWithdrawalDate();	// 退学年月日
			if(withdrawalDate != null) {
				int withdrawalStartDay = YearMonth.of(year, month).lengthOfMonth() + 1;
				// withdrawalDateが指定年月と同じ場合、退学日の次の日から 8
				if (YearMonth.from(withdrawalDate.toLocalDate()).equals(YearMonth.of(year, month))) {
					withdrawalStartDay = withdrawalDate.getDayOfMonth() + 1;
				}
				// 指定年月がwithdrawalDateを経過している場合、全日 8
				else if(withdrawalDate.toLocalDate().isBefore(YearMonth.of(year, month).atEndOfMonth())) {
					withdrawalStartDay = 1;
				}
				// 指定日から月末まで 8
				for(int i = withdrawalStartDay; i <= YearMonth.of(year, month).lengthOfMonth(); i++) {
					studentAttendMap.get(student.getStudentId()).put(i, 8);
				}
			}
		}

		// 合計欠席数を計算
		if(useAttendSum) {
			for(Map.Entry<Integer, HashMap<Integer, Integer>> attends: studentAttendMap.entrySet()) {
				for(Map.Entry<Integer, Integer> attend: attends.getValue().entrySet()) {
					Double attendSum = studentAttendSumMap.get(attends.getKey());
					if(attend.getValue() == 1) {	// 欠席
						attendSum += 1.0;
					} else if(attend.getValue() == 2 || attend.getValue() == 3 || attend.getValue() == 23) {	// 遅刻・早退・遅刻＋早退
						attendSum += 0.3;	// 遅刻＋早退は、後から+0.3
					}
					// x.9以上の場合は切り上げ
					if((attendSum - Math.floor(attendSum)) > 0.899) {	// >= 0.9 は、Double型の誤差が生じるので近似値で比較
						attendSum = Math.ceil(attendSum);
					}

					if(attend.getValue() == 23) {	// 遅刻＋早退の場合は、もう一度+0.3 ※Double型は小数の誤差が出るので、0.3ずつ加算する
						attendSum += 0.3;
						// x.9以上の場合は切り上げ
						if((attendSum - Math.floor(attendSum)) > 0.899) {	// >= 0.9 は、Double型の誤差が生じるので近似値で比較
							attendSum = Math.ceil(attendSum);
						}
					}

					studentAttendSumMap.put(attends.getKey(), attendSum);
				}
			}
		}

		// 出席状況を学生情報一覧Mapに追加
		for(Map.Entry<Integer, HashMap<Integer, Integer>> studentAttend: studentAttendMap.entrySet()) {
			for(Map.Entry<Integer, Integer> attend: studentAttend.getValue().entrySet()) {
				studentFieldsMap.get(studentAttend.getKey()).put(String.valueOf(attend.getKey()), String.valueOf(attend.getValue()));
			}
		}

		// 合計出席数を学生情報一覧Mapに追加
		for(Map.Entry<Integer, Double> studentAttendSum: studentAttendSumMap.entrySet()) {
			studentFieldsMap.get(studentAttendSum.getKey()).put("attend_sum", String.format("%.1f", studentAttendSum.getValue()));
		}

		return studentFieldsMap;
	}
}
