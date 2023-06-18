<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

	<head>
		<title>Glasanje</title>
	</head>
	
	<body>
	
		<h1>Odaberite poll u kojem želite glasati:</h1>
		<ol>
			<c:forEach var="poll" items="${polls}">
				<li>
					<h2>${poll.title}</h2>
					<a href="glasanje?pollID=${poll.id}">${poll.message}</a>
				</li>
			</c:forEach>
		
		</ol>
	</body>
	
</html>
