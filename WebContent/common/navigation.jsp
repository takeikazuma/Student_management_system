<%-- サイドバー --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

    <style>
        /* サイドメニューのスタイル（余裕があればcssファイルに外だししたい） */
        .side-menu {
            position: fixed;
            top: 0;
            left: -250px; /* メニューを初期状態で隠す */
            width: 250px;
            height: 100%;
            background-color: #343a40;
            transition: left 0.3s ease;
            z-index: 1050;
        }

        .side-menu ul {
            list-style-type: none;
            padding: 0;
            margin: 0;
        }

        .side-menu ul li {
            padding: 5px;
            color: white;
        }

        .side-menu ul li a {
            color: white;
            text-decoration: none;
            display: block;
        }

        .side-menu ul li a:hover {
            background-color: #495057;
        }

        /* メニューが表示されているとき */
        .side-menu.show {
            left: 0; /* メニューをスライドして表示 */
        }

    </style>

	<!-- メニューボタンは header.jsp に配置 -->
    <div class="side-menu" id="side-menu">
        <ul>
        	<!-- 横向きの折り畳みメニュー -->
            <li class="nav-item my-3"><a href="menu.jsp">メニュー</a></li>
            <li class="nav-item mb-3">◇ 出席管理 ◇</li>
            <li class="nav-item mx-3 mb-3"><a href="input_attend.jsp">・ 出欠席</a></li>
            <li class="nav-item mx-3 mb-3"><a href="all_attend.jsp">・ 出欠席一覧表示</a></li>
            <li class="nav-item mx-3 mb-3"><a href="absence.jsp">・ 長期休暇登録</a></li>
			<li class="nav-item mb-3">◇ 成績管理 ◇</li>
			<li class="nav-item mx-3 mb-3"><a href="scoreIn.jsp">・ 成績入力</a></li>
			<li class="nav-item mx-3 mb-3"><a href="scoreOut.jsp">・ 成績出力</a></li>
			<li class="nav-item mb-3"><a href="instruction.jsp"> 指導表入力</a></li>
			<li class="nav-item mb-3"><a href="changePassword.jsp"> パスワード変更</a></li>
            <li><a href="#" id="menu-toggle-close">閉じる</a></li>
        </ul>
    </div>

    <script>
        // メニューボタンをクリックしたときにメニューの表示・非表示を切り替え
        document.getElementById('menu-toggle').addEventListener('click', function() {
            var menu = document.getElementById('side-menu');
            menu.classList.toggle('show');
        });
        document.getElementById('menu-toggle-close').addEventListener('click', function() {
            var menu = document.getElementById('side-menu');
            menu.classList.toggle('show');
        });
    </script>