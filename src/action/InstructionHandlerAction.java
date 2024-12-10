package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;
/**
 * @author k_arita
 *
 */
public class InstructionHandlerAction extends Action  {

    public void execute(HttpServletRequest req, HttpServletResponse res)  throws Exception {

        String actionType = req.getParameter("operation"); // リクエストからどの操作がされたかを取得

        try {
            if ("delete".equals(actionType)) {
            	//delete操作の場合

            } else if ("register".equals(actionType)) {
                // 他のアクションクラス（例: RegisterInstructionAction）を呼び出し
                RegistrationInstructionAction registerAction = new RegistrationInstructionAction();
                registerAction.execute(req, res);
            } else {
                // 不正なアクションの場合のエラーハンドリング
                req.setAttribute("message", "無効な操作が指定されました");

            }

            // フォワード処理を最後に。
            req.getRequestDispatcher("instruction.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "エラーが発生しました");
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
