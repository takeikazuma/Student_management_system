package action;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Instruction;
import bean.Student;
import dao.InstructionDao;
import dao.StudentDao;
import tool.Action;

public class ExportInstructionAction extends Action {

    // 定数
    private static final String COMMA = ",";
    private static final String NEWLINE = "\r\n";
    private static final String DQ = "\"";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    	try {

    		//リクエストパラメータ―の取得
            String studentIdStr = req.getParameter("studentIdHidden");
            int studentId = Integer.parseInt(studentIdStr);

            // 学生データの取得
            StudentDao studentDao = new StudentDao();
            List<Student> studentList = studentDao.getStudent(studentId);

            if (studentList.isEmpty()) {
                req.setAttribute("message", "出力対象のデータが存在しません。");
                return;
            }

            // 指導データの取得
            InstructionDao instructionDao = new InstructionDao();
            List<Instruction> instructionList = instructionDao.getInstruction(studentId);

            if (instructionList.isEmpty()) {
                req.setAttribute("message", "指導データが存在しません。");
                return;
            }

            // CSV生成
            String studentName = studentList.get(0).getStudentName();
            generateCsv(res, studentIdStr, studentName, instructionList);

        } catch (NumberFormatException e) {
            req.setAttribute("message", "無効な学生番号が入力されました。");
        } catch (Exception e) {
            req.setAttribute("message", "データの処理中にエラーが発生しました。");
            //↓やらないほうがいい？？
            e.printStackTrace();
        }
    }

    private void generateCsv(HttpServletResponse res, String studentIdStr, String studentName, List<Instruction> instructionList) throws Exception {

        String filename = "指導表.csv";
        res.setHeader("Content-Type", "text/csv; charset=Shift_JIS");
        res.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(studentName + "_" + filename, "UTF-8") + "\"");

        try (PrintWriter out = res.getWriter()) {

            // ヘッダの生成
            out.append("学生番号,学生氏名,日付,入力者,内容").append(NEWLINE);

            // 行データの生成
            StringBuilder row = new StringBuilder();

            for (Instruction i : instructionList) {
            	row.setLength(0); // 初期化
            	row.append(addDoubleQuotation(studentIdStr)).append(COMMA)
                          .append(addDoubleQuotation(i.getStudentName())).append(COMMA)
                          .append(addDoubleQuotation(String.valueOf(i.getInputDate()))).append(COMMA)
                          .append(addDoubleQuotation(i.getUsersName())).append(COMMA)
                          .append(addDoubleQuotation(i.getInstructions())).append(NEWLINE);
                //一行分のデータをCSV用のデータに追記
            	out.append(row.toString());
            }
        }
    }

    private String addDoubleQuotation(String content) {
    	//ダブルクォーテーションで囲む
        return DQ + content + DQ;
    }
}

