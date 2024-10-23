package tool;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = { "/*" })
public class EncodingFilter implements Filter {
	/**
	 * doFilterメソッド フィルター処理を記述
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 文字コードをセット
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		// System.out.println("フィルタの前処理");
		chain.doFilter(request, response);
		// System.out.println("フィルタの後処理");
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
