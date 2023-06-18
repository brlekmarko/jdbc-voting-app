<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

	<head>
		<title>Glasanje</title>
	</head>
	
	<body>
	
		<h1><c:out value="${poll.title}"></c:out></h1>
		<p><c:out value="${poll.message}"></c:out></p>
		
		<ol>
			<c:forEach var="option" items="${options}">
				<li>
					<a href="glasanje-glasaj?optionID=${option.id}&pollID=${option.pollID}">${option.optionTitle}</a>
				</li>
			</c:forEach>
		
		</ol>
	</body>
	
</html>
