<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="com.fss.plugin.iPayPipe"%>
<%@page import="java.io.File"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta name="GENERATOR" content="IBM WebSphere Studio">
<link href="<%=request.getContextPath()%>/css/main.css" type=text/css rel=stylesheet>
<title>Canon - Hosted</title>
</head>
<body class="bg">
<br>
<table align=center border=1  bordercolor=black>
	<tr>
		<td>
			<table align=center border=0  bordercolor=black>
				<tr>
					<td colspan="2" align="center">
						<font size="4"><B>Transaction Details   </B></font>
					</td>
				</tr>
				<%
					Properties properties = null;
					FileInputStream inStream = null;
					File inputFile = null;
					File basePath = null;
					String xml = null;
				 	try {
				 		properties = new Properties();
						basePath = new File(pageContext.getServletContext().getRealPath("/"));
						inputFile = new File(basePath+"/jsp/properties/", "transaction.properties");
						inStream = new FileInputStream(inputFile);
						properties.load(inStream);
					}catch (Exception e) {
						e.printStackTrace();
						properties = null;
					}finally{
				 		try {
				 			if(inStream!=null){
								inStream.close();
								inStream = null;
							}
							if (inputFile != null){
								inputFile = null;
							}
						}catch (Exception e) {
							properties = null;
						}
				 	}
					iPayPipe pipe = new iPayPipe(); 
					pipe.setKeystorePath(properties.getProperty("resourcePath"));
					pipe.setAlias(properties.getProperty("aliasName"));
					pipe.setResourcePath(properties.getProperty("resourcePath"));
					//xml = properties.getProperty("xmlData");
					int result=	pipe.parseEncryptedRequest(request.getParameter("trandata"));
					String errorText=request.getParameter("ErrorText");
				%>
						<c:set var="result" scope="request" value="<%=result%>"/>
						<c:set var="error" scope="request" value="<%=errorText%>"/>
						<c:choose>
						<c:when test="${result!=0}" >
						<tr>
									<td>
										Error
									</td>
									<td>
										&nbsp;&nbsp;
										<b><font size="2" color="red"><%=pipe.getError()%></font>
										</b>
									</td>
								</tr>
							</c:when>	
							<c:otherwise>
							<c:choose>
							<c:when test="${error==null}">
							<tr>
									<td>
										Transaction Status
									</td>
									<td>
										&nbsp;&nbsp;
										<b><font size="2" color="red"><%=pipe.getResult()%></font>
										</b>
									</td>
								</tr>
								<tr>
									<td>
										Post Date
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getDate()%></td>
								</tr>
								<tr>
									<td>
										Transaction Reference ID
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getRef()%></td>
								</tr>
								<tr>
									<td>
										Mrch Track ID
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getTrackId()%></td>
								</tr>
								<tr>
									<td>
										<b>Transaction ID</b>
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getTransId()%></td>
								</tr>
								<tr>
									<td>
										Transaction Amount
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getAmt()%></td>
								</tr>
								<tr>
									<td>
										UDF5
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getUdf5()%></td>
								</tr>
								<tr>
									<td>
										UDF3
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getUdf3()%></td>
								</tr>
								<tr>
									<td>
										UDF4
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getUdf4()%></td>
								</tr>
								<tr>
									<td>
										Payment ID
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getPaymentId()%></td>
								</tr>
								<tr>
									<td>
										Customer ID
									</td>
									<td>
										&nbsp;&nbsp;<%=pipe.getCustid()%></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>
										ErrorText
									</td>
									<td>
										&nbsp;&nbsp;<%=request.getParameter("ErrorText")%></td>
								</tr>
							</c:otherwise>
							</c:choose>
							</c:otherwise>	
				</c:choose>
					</table>
				</td>
			</tr>
		</table>
		<br>
		<table align=center>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td>
					<font size=2 color="BLUE"><A href="TranPipeIndex.jsp">Tranportal
							Transaction</A>
					</font>
				</td>
			</tr>
			<tr>
				<td>
					<font size=2 color="BLUE"><A href="HostedPaymentIndexHttp.jsp">Hosted
							Transaction</A>
					</font>
				</td>
			</tr>
		</table>
	</body>
</html>