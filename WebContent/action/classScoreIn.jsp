<%-- 成績管理JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts">
		<script type="text/javascript">
			$(function() {
				// submitボタンクリック時のサブミット処理
				$("#test-submit-button").click(function() {
					$("#test-form").submit();
				});

				// 成績入力欄を取得
				let input = $("input[type='number']");
				// キーボード押下時の処理
				input.on("keydown", function(e) {
					// 成績入力欄の個数を取得
					let n = input.length;
					// Enterキーが押された場合
					if (e.which == 13) {
						e.preventDefault();
						// 入力欄番号を取得
						let Index = input.index(this);
						// 次の入力欄番号を取得
						let nextIndex = input.index(this) + 1;
						// 次の入力欄番号が個数以下場合
						if (nextIndex < n) {
							// 次の要素へフォーカスを移動
							input[nextIndex].focus();
							// 次の要素を全選択
							input[nextIndex].select();
						} else {
							// 最後の要素ではフォーカスを外す
							input[Index].blur();
						}
					}
				});
			});
		</script>
	</c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

			<form method="get">
				<div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
					<div class="col-2">
						<label class="form-label" for="test-f1-select">入学年度</label>
						<select class="form-select " id="test-f1-select" name="f1">
							<option value="0">--------</option>
							<c:forEach var="year" items="${ent_year_set}">
								<%-- 現在のyearと選択されていたf1が一致していた場合selectedを追記 --%>
								<option value="${year}" <c:if test="${year==f1}">selected</c:if>>${year}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2">
						<label class="form-label" for="test-f2-select">クラス</label>
						<select class="form-select " id="test-f2-select" name="f2">
							<option value="0">--------</option>
							<c:forEach var="num" items="${class_num_set}">
								<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>
								<option value="${num}" <c:if test="${num==f2}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-4">
						<label class="form-label" for="test-f3-select">科目</label>
						<select class="form-select " id="test-f3-select" name="f3">
							<option value="0">--------</option>
							<c:forEach var="subject" items="${subjects}">
								<%-- 現在のsubject.cdと選択されていたf3が一致していた場合selectedを追記 --%>
								<option value="${subject.cd}" <c:if test="${subject.cd==f3}">selected</c:if>>${subject.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2">
						<label class="form-label" for="test-f3-select">回数</label>
						<select class="form-select " id="test-f4-select" name="f4">
							<option value="0">--------</option>
							<c:forEach var="num" items="${num_set}">
								<%-- 現在のnumと選択されていたf4が一致していた場合selectedを追記 --%>
								<option value="${num}" <c:if test="${num==f4}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">検索</button>
					</div>
					<div class="mt-2 text-warning">${errors.get("filter")}</div>
				</div>
			</form>
			<c:choose>
				<%-- 成績情報が存在する場合 --%>
				<c:when test="${tests.size()>0}">
					<%-- パラメーターdoneが存在する場合 --%>
					<c:if test="${!empty done}">
						<div class="bg-success bg-opacity-50 text-center lh-lg">
							<p>${done}</p>
						</div>
					</c:if>
					<form method="post" id="test-form" action="TestRegistExecute.action">
						<div>科目：${subject.name}（${num}回）</div>
						<table class="table table-hover">
							<tr>
								<th>入学年度</th>
								<th>クラス</th>
								<th>学生番号</th>
								<th>氏名</th>
								<th>点数</th>
								<!--
								<th class="text-center">削除</th>
								 -->
							</tr>
							<c:forEach var="test" items="${tests}">
								<tr>
									<td>${test.student.entYear}</td>
									<td>${test.student.classNum}</td>
									<td>${test.student.no}</td>
									<td>${test.student.name}</td>
									<td>
										<!-- 登録する得点を学生番号を用いて取得できるようにする -->
										<input type="number" name="point_${test.student.no}"
											<c:choose>
												<%-- 入力された得点用マップに現在のstudent.noが含まれている場合 --%>
												<c:when test="${input_points.containsKey(test.student.no)}">
													<%-- 入力されていた得点を初期表示 --%>
													value="${input_points.get(test.student.no)}"
												</c:when>
												<%-- 成績にデータが存在する場合 --%>
												<c:when test="${test.point!=-1}">
													<%-- 登録されている得点を初期表示 --%>
													value="${test.point}"
												</c:when>
											</c:choose> />
										<div class="mt-2 text-warning">${errors.get(test.student.no)}</div>
										<!-- 登録する学生番号を一覧として送る -->
										<input type="hidden" name="student_no_set[]" value="${test.student.no}" />
									</td>
									<td class="text-center">
										<!-- 削除する成績を学生番号を用いて取得できるようにする -->
										<!--
										<input class="form-check-input" type="checkbox" name="delete_${test.student.no}" />
										 -->
									</td>
								</tr>
							</c:forEach>
						</table>
						<input type="hidden" id="test-subject_cd-hidden" name="subject_cd" value="${subject.cd}" />
						<input type="hidden" id="test-num-hidden" name="num" value="${num}" />
						<input type="hidden" id="test-f1-hidden" name="f1" value="${f1}" />
						<input type="hidden" id="test-f2-hidden" name="f2" value="${f2}" />
						<input type="hidden" id="test-f3-hidden" name="f3" value="${f3}" />
						<input type="hidden" id="test-f4-hidden" name="f4" value="${f4}" />
						<div class="mt-3">
							<!--
							<input class="btn btn-primary" type="submit" value="登録して再度入力" name="continue">
							 -->
							<input class="btn btn-secondary" type="submit" value="登録して終了" name="end" />
						</div>
					</form>
				</c:when>
				<%-- 成績情報が存在しない場合 --%>
				<c:when test="${tests.size()==0}">
					<div>学生情報が存在しませんでした</div>
				</c:when>
			</c:choose>
		</section>
	</c:param>
</c:import>

