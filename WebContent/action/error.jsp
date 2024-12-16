<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" --%>
<%--     pageEncoding="UTF-8"%> --%>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!-- <html> -->
<!-- <head> -->
<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
<!-- <title>Insert title here</title> -->
<!-- </head> -->
<!-- <body> -->

<!-- </body> -->
<!-- </html> -->


<%-- エラーページ --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- Bootstrap CSS -->
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
		crossorigin="anonymous">
	<title>エラーページ</title>
	<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	${param.scripts}
</head>
<body>
	<div id="wrapper" class="container">
		<header
			class="d-flex flex-wrap justify-content-center py-3 px-5 mb-4 border-bottom border-2 bg-primary bg-opacity-10 bg-gradient">
			<div class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
			    <h1 class="fs-3">経理本科教務システム</h1>
			</div>
			<c:if test="${user.isAuthenticated()}">
				<div class="nav align-self-end">
			        <span class="nav-item px-2">${users.getUsersName()}様</span>
			        <!-- ログアウトリンクにJavaScriptの確認ダイアログを追加 -->
			        <a class="nav-item px-2" href="Logout.action" onclick="return confirmLogout()">ログアウト</a>
			    </div>
			</c:if>
		</header>

		<div class="row justify-content-center">
			<c:choose>
				<%-- ログイン済みの場合 --%>
				<c:when test="${users.isAuthenticated()}">
					<nav class="col-2" style="height:40rem;">
						<c:import url="/common/navigation.jsp" />
					</nav>
					<main class="col-10 border-start">
						<section>
							<p class="my-2 text-danger">エラーが発生しました</p>
							<p class="my-2 text-danger">${message}</p>
						</section>
					</main>
				</c:when>
				<%-- 未ログインの場合 --%>
				<c:otherwise>
					<main class="col-12">
						<section>
							<p class="my-2 text-danger">エラーが発生しました</p>
							<p class="my-2 text-danger">${message}</p>
						</section>
					</main>
				</c:otherwise>
			</c:choose>
		</div>
		<footer	class="py-2 my-4 bg-dark bg-opacity-10 border-top border-3 align-bottom">
			<p class="text-center text-muted mb-0"><small>&copy; 2023 TIC 大原学園</small></p>
		</footer>

	</div>
</body>
</html>
