<%-- ヘッダー --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
    <h1 class="fs-3">経理本科教務システム</h1>
</div>

<c:if test="${users.isAuthenticated()}">
    <div class="nav align-self-end">
        <span class="nav-item px-2">${users.getUsersName()}様</span>
        <!-- ログアウトリンクにJavaScriptの確認ダイアログを追加 -->
        <a class="nav-item px-2" href="Logout.action" onclick="return confirmLogout()">ログアウト</a>
    </div>
</c:if>

<script>
    function confirmLogout() {
        return confirm("本当にログアウトしますか？");
    }
</script>
