package action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AttendDao;
import tool.Action;

public class RegistrationAttendAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ローカル変数の宣言 1
		AttendDao attendDao = new AttendDao();

		//リクエストパラメータ―の取得 2

	    // パラメーター名の列挙を取得
        Enumeration<String> parameterNames = req.getParameterNames();

        // パラメーターをループで処理
        while (parameterNames.hasMoreElements()) {
            // パラメーターの値をスプリットして分割
            String part = parameterNames.nextElement();
            String[] parts = part.split("_");
            if(parts.length == 2 && parts[1].length() == 8 && parts[1].matches("\\d+")) {
                // 上7桁のコードは学生番号
	            Integer studentId = Integer.valueOf(parts[0]);

	            // 下8桁の日付部分
	            String datePart = parts[1];
	            // 最初の4桁:年、次の2桁:月、最後の2桁:日
	            String attendDate = String.join("-", datePart.substring(0, 4), datePart.substring(4, 6), datePart.substring(6, 8)) + " 00:00:00";
	            // 出欠席
	            Integer status = Integer.valueOf(req.getParameter(part));
	            // 出席状況を保存
	            attendDao.saveAttend(studentId, attendDate, status);
            }
        }
        // SelectAttendActionを実行して検索結果画面を表示させる
        SelectAttendAction selectAttendAction = new SelectAttendAction();
        selectAttendAction.execute(req, res);
	}
}
