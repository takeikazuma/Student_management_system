<%-- 出欠席入力JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">
        経理本科教務システム
    </c:param>

    <c:param name="scripts">
        <script type="text/javascript">
            $(function() {
                // カスタムバリデーションメッセージを設定
                $('#admission_year').on('invalid', function() {
                    this.setCustomValidity('入学年度を入力してください。');
                });
                $('#admission_year').on('input', function() {
                    this.setCustomValidity('');
                });

                $('#class_name').on('invalid', function() {
                    this.setCustomValidity('クラス名を入力してください。');
                });
                $('#class_name').on('input', function() {
                    this.setCustomValidity('');
                });

                $('#year').on('invalid', function() {
                    this.setCustomValidity('抽出年を入力してください。');
                });
                $('#year').on('input', function() {
                    this.setCustomValidity('');
                });

                $('#month').on('invalid', function() {
                    this.setCustomValidity('抽出月を入力してください。');
                });
                $('#month').on('input', function() {
                    this.setCustomValidity('');
                });

                $('#day').on('invalid', function() {
                    this.setCustomValidity('抽出日を入力してください。');
                });
                $('#day').on('input', function() {
                    this.setCustomValidity('');
                });
            });
        </script>
    </c:param>

    <c:param name="content">
        <style>
            /* 数値の上下ボタンを非表示にするCSS */
            .no-spinner::-webkit-outer-spin-button,
            .no-spinner::-webkit-inner-spin-button {
                -webkit-appearance: none;
                margin: 0;
            }

            .no-spinner {
                -moz-appearance: textfield;
            }
        </style>
        <section class="w-75 text-center m-auto border pb-3">
            <form action="SelectAttend.action" method="post">
                <div id="wrap_box">
                  <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">出欠席入力</h2>
                    <div>
                        <div class="form-floating mx-5">
                            <input class="form-control px-5 fs-5 no-spinner" autocomplete="off"
                                   id="admission_year" name="admission_year" maxlength="4" placeholder="入学年度を入力してください"
                                   style="ime-mode: disabled" type="number" value="${admission_year}" required />
                            <label>入学年度</label>
                        </div>
                        <div class="form-floating mx-5">
                            <input class="form-control px-5 fs-5 no-spinner" autocomplete="off"
                                   id="class_name" name="class_name" placeholder="クラス名を入力してください"
                                   style="ime-mode: disabled" type="text" value="${class_name}" required />
                            <label>クラス名</label>
                        </div>
                        <div class="form-floating mx-5">
                            <input class="form-control px-5 fs-5 no-spinner" autocomplete="off"
                                   id="year" name="year" maxlength="4" placeholder="抽出年を入力してください"
                                   style="ime-mode: disabled" type="number" value="${year}" required />
                            <label>抽出年</label>
                        </div>
                        <div class="form-floating mx-5">
                            <input class="form-control px-5 fs-5 no-spinner" autocomplete="off"
                                   id="month" name="month" maxlength="2" placeholder="抽出月を入力してください"
                                   style="ime-mode: disabled" type="number" value="${month}" required />
                            <label>抽出月</label>
                        </div>
                        <div class="form-floating mx-5">
                            <input class="form-control px-5 fs-5 no-spinner" autocomplete="off"
                                   id="day" name="day" maxlength="2" placeholder="抽出日を入力してください"
                                   style="ime-mode: disabled" type="number" value="${day}" required />
                            <label>抽出日</label>
                        </div>
                    </div>

                    <div class="mt-4">
                        <input class="w-25 btn btn-lg btn-primary" type="submit" name="login" value="出欠席入力" />
                    </div>
                </div>
            </form>
			<c:if test="${attendMap.size() > 0}">
	            <form action="RegistrationAttend.action" method="post">
	                <%-- 検索項目をhidden項目で保持 --%>
		            <input id="admission_year" name="admission_year" type="hidden" value="${admission_year}" />
					<input id="class_name" name="class_name" type="hidden" value="${class_name}" />
					<input id="year" name="year" type="hidden" value="${year}" />
					<input id="month" name="month" type="hidden" value="${month}" />
					<input id="day" name="day" type="hidden" value="${day}" />
					<div>${year}年${month}月${day}日 出欠席一覧</div>
					<table>
						<tr>
							<td>入学年度</td>
							<td>クラス</td>
							<td>氏名</td>
							<td>出欠席</td>
						</tr>
						<%-- 抽出学生を繰り返し --%>
						<c:forEach var="studentFields" items="${studentFieldsMap}">
						<tr>
							<td>${studentFields.value.get("admission_year")}</td>
							<td>${studentFields.value.get("grade_class_name")}</td>
							<td>${studentFields.value.get("student_name")}</td>
							<td>
								<select name="${studentFields.key}_${String.format("%04d",year)}${String.format("%02d",month)}${String.format("%02d",day)}">
								    <option value="0" <c:if test="${attendMap.get(studentFields.key).get(day) == 0}">selected</c:if>>出</option>
								    <option value="1" <c:if test="${attendMap.get(studentFields.key).get(day) == 1}">selected</c:if>>欠</option>
								    <option value="2" <c:if test="${attendMap.get(studentFields.key).get(day) == 2}">selected</c:if>>遅</option>
								    <option value="3" <c:if test="${attendMap.get(studentFields.key).get(day) == 3}">selected</c:if>>早</option>
								</select>
							</td>
		                </tr>
						</c:forEach>
		            </table>
		            <div class="mt-4">
		                <input class="w-25 btn btn-lg btn-primary" type="submit" name="login" value="登録" />
		            </div>
	            </form>
	        </c:if>

        </section>
    </c:param>
</c:import>