<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

	<head>
		<style>
			body {
				background-color: <% 
				if(session.getAttribute("pickedBgCol") != null){
					out.println(session.getAttribute("pickedBgCol").toString());
				} else{
					out.println("white");				
				}
				%>
			}
		</style>
		
		<title>Invalid args</title>
	</head>
	
	<body>
	
		<h1>Invalid arguments!</h1>
		
	</body>
	
</html>
