<%-- 出欠席一覧表示JSP --%>
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

                // ユーザIDの桁数制限（10桁以内）
                $('#id-input').on('input', function() {
                    const maxDigits = 9;
                    if ($(this).val().length > maxDigits) {
                        $(this).val($(this).val().slice(0, maxDigits));

                    }
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
            <form action="AllAttendRegist.action" method="post">
                <div id="wrap_box">
                  <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2">出欠席一覧表示</h2>
                    <c:if test="${errors.size() > 0}">
                        <div>
                            <c:forEach var="error" items="${errors}">
                                ${error}
                            </c:forEach>
                        </div>
                    </c:if>
                    <div>
						<table><tr>
	                        <td class="form-floating mx-1">
	                            <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
	                                   id="admission_year" name="admission_year" maxlength="4" placeholder="入学年度を入力してください"
	                                   style="ime-mode: disabled" type="number" value="${admission_year}" required />
	                            <label>入学年度</label>
	                        </td>
	                        <td class="form-floating mx-1">
	                            <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
	                                   id="class_name" name="class_name" placeholder="クラス名を入力してください"
	                                   style="ime-mode: disabled" type="text" value="${class_name}" required />
	                            <label>クラス名</label>
	                        </td>
	                        <td class="form-floating mx-1">
	                            <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
	                                   id="year" name="year" maxlength="4" placeholder="抽出年を入力してください"
	                                   style="ime-mode: disabled" type="number" value="${year}" required />
	                            <label>抽出年</label>
	                        </td>
	                        <td class="form-floating mx-1">
	                            <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
	                                   id="month" name="month" maxlength="2" placeholder="抽出月を入力してください"
	                                   style="ime-mode: disabled" type="number" value="${month}" required />
	                            <label>抽出月</label>
	                        </td>
	                        <td class="mx-1">
		                        <input class="btn btn-lg btn-primary" type="submit" name="login" value="表示" />
	                        </td>
						</tr></table>
                    </div>
                </div>
            </form>
        </section>

	        <c:if test="${studentFieldsMap.size() > 0}">
				<div>${year}年${month}月 出欠席一覧</div>
				<table style="width:100%;text-align:center">
					<tr>
						<td style="border: 1px solid black;">氏名</td>
						<c:forEach var="day" begin="1" end="${length_of_month}">
							<td style="border: 1px solid black;">${day}</td>
			            </c:forEach>
			            <td style="border: 1px solid black;">合計</td>
					</tr>
					<%-- 抽出学生を繰り返し --%>
					<c:forEach var="studentFields" items="${studentFieldsMap}">
						<tr>
							<td style="border: 1px solid black;">${studentFields.value.get("student_name")}</td>
							<%-- 指定月の全ての日を繰り返し --%>
							<c:forEach var="day" begin="1" end="${length_of_month}">
								<td style="border: 1px solid black;">${(attendMap.get(studentFields.key).get(day)==1)?"欠":(attendMap.get(studentFields.key).get(day)==2)?"遅":(attendMap.get(studentFields.key).get(day)==3)?"早":""}</td>
							</c:forEach>
							<td style="border: 1px solid black;">${studentAttendSumMap.get(studentFields.key)}</td>
		                </tr>
					</c:forEach>

	            </table>
	        </c:if>

    </c:param>
</c:import>