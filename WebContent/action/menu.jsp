<%-- メニューJSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title"></c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">メニュー</h2>
			<div class="row text-center px-4 fs-3 my-5">
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #dbb;">
					<a href="#">出欠席</a>
				</div>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #bdb;">
					<div>
						<div class="">成績管理</div>
						<div class="">
							<a href="TestRegist.action">成績入力</a>
						</div>
						<div class="">
							<a href="TestList.action">成績出力</a>
						</div>
					</div>
				</div>
				<div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
					style="height: 10rem; background-color: #bbd;">
					<a href="#">指導表入力</a>
				</div>
			</div>
		</section>
	</c:param>
</c:import>