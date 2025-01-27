package action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllAttendOutputAction extends AllAttendRegistAction {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		List<String> errors = new ArrayList<>();

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

			/** (暫定的に) CSV出力 **/
	       // レスポンスの設定（Shift-JIS指定）
	        res.setContentType("text/csv; charset=Shift_JIS");
	        res.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode("attend_list.csv", "Shift_JIS") + "\"");

	        // Shift-JIS対応のOutputStreamWriterを使用
	        OutputStreamWriter writer = new OutputStreamWriter(res.getOutputStream(), "Shift_JIS");
	        PrintWriter out = new PrintWriter(writer);

			if(errors.size() <= 0) {
				/** 正常処理 **/
	        	int lengthOfMonth = YearMonth.of(year, month).lengthOfMonth();
	        	List<String> csvLine = new ArrayList<>();
	        	// 項目名
	        	csvLine.add("氏名");
			    for(int day = 1; day <= lengthOfMonth; day++) {
		    		csvLine.add(String.valueOf(day));
			    }
			    csvLine.add("合計");
			    out.println(String.join(",", csvLine));

		        for(Entry<Integer, HashMap<String, String>> studentFields: studentFieldsMap.entrySet()) {
		        	csvLine.clear();
		        	csvLine.add(studentFields.getValue().get("student_name"));
				    for(int day = 1; day <= lengthOfMonth; day++) {
				    	int attendData = 0;
				    	try {
					    	attendData = Integer.valueOf(studentFields.getValue().get(String.valueOf(day)));
				    	} catch(Exception e) {
				    		attendData = 0;
				    	}
				    	switch(attendData) {
				    	case 9:
				    		csvLine.add("-");
				    		break;
				    	case 8:
				    		csvLine.add("退");
				    		break;
				    	case 1:
				    		csvLine.add("欠");
				    		break;
				    	case 2:
				    		csvLine.add("遅");
				    		break;
				    	case 3:
				    		csvLine.add("早");
				    		break;
				    	case 23:
				    		csvLine.add("遅早");
				    		break;
			    		default:
			    			csvLine.add("");
				    	}
				    }
				    csvLine.add(studentFields.getValue().get("attend_sum"));
				    out.println(String.join(",", csvLine));
		        }
			}
			else {
				/** エラー処理 **/
		        // エラーメッセージをCSVに出力
		        for(String errorMessage: errors) {
				    out.println(errorMessage);
		        }
			}
		    // PrintWriterのクローズ
		    out.flush();
		    out.close();
		}
	}
}
