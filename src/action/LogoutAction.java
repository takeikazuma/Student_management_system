package chapter24;

import tool.Action;
import javax.servlet.http.*;

public class LogoutAction extends Action {
	public String execute(
		HttpServletRequest request, HttpServletResponse response
	) throws Exception {

		HttpSession session=request.getSession();

		if (session.getAttribute("customer")!=null) {
			session.removeAttribute("customer");
			return "logout-out.jsp";
		}
		
		return "logout-error.jsp";
	}
}
