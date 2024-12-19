<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
    <c:param name="title">経理本科教務システム</c:param>
    <c:param name="content">
        <section class="me-4">
            <form action="registerAbsence" method="post">
                <div id="wrap_box">
                    <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">長期休暇登録</h2>
                    <c:if test="${message != null}">
                        <div class="result bg-success bg-opacity-50 text-center lh-lg">${message}</div>
                    </c:if>

                    <div>
                        <table>
                            <tr>
                                <td class="form-floating mx-1">
                                    <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
                                           id="admission_year" name="admission_year" maxlength="4"
                                           placeholder="入学年度を入力してください" type="number" required />
                                    <label>入学年度</label>
                                </td>

                                <td class="form-floating mx-1">
                                    <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
                                           id="class_name" name="class_name" placeholder="クラス名を入力してください"
                                           type="text" required />
                                    <label>クラス名</label>
                                </td>

                                <td class="form-floating mx-1">
                                    <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
                                           id="start_date" name="start_date" placeholder="開始日" type="date" required />
                                    <label>開始日</label>
                                </td>

                                <td class="form-floating mx-1">
                                    <input class="form-control px-4 fs-5 no-spinner" autocomplete="off"
                                           id="end_date" name="end_date" placeholder="終了日" type="date" required />
                                    <label>終了日</label>
                                </td>

                                <td class="mx-1">
                                    <input class="btn btn-lg btn-primary" type="submit" value="登録" />
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </form>
        </section>
    </c:param>
</c:import>
