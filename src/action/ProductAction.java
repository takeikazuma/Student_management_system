package chapter25;

import bean.Product;
import dao.ProductDAO;
import tool.Action;
import javax.servlet.http.*;
import java.util.List;

public class ProductAction extends Action {
	public String execute(
		HttpServletRequest request, HttpServletResponse response
	) throws Exception {

		HttpSession session=request.getSession();

		String keyword=request.getParameter("keyword");
		if (keyword==null) keyword="";

		ProductDAO dao=new ProductDAO();
		List<Product> list=dao.search(keyword);

		session.setAttribute("list", list);

		return "product.jsp";
	}
}
