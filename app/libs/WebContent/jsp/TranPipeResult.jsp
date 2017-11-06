<%@page import="java.util.Properties"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="com.fss.plugin.iPayPipe"%>

<HTML>
<head>
</head>
	<body class="bg" >
		<br><br>
		<table align=center border=1 bordercolor=black>
			<tr>
				<td>
					<table align=center border=0 bordercolor=black>
						<tr>
							<td colspan="2" align="center">
								<font size="4"><B>Transaction Details</B></font>
							</td>
						</tr>
						<%
							iPayPipe pipe = new iPayPipe();
							boolean flag = false;
							
							String errorText=request.getParameter("ErrorText");
							flag =	pipe.parseEncryptedRawResult(request.getParameter("responseData")) == 0;
							if((errorText != null && !errorText.trim().isEmpty()) ){
						%>
								<tr>
									<td>
										Error
									</td>
									<td>
										&nbsp;&nbsp;
										<b><font size="2" color="red"><%=pipe.getError()%></font><b>
										<font size="2" color="red"><%=errorText%></font>
										</b>
									</td>
								</tr>
						<%		
							}else{
						%>					
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
						<%
							} 
						%>
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
					<font size=2 color="BLUE"><a href="TranPipeIndex.jsp">Tranportal
							Transaction</a>
					</font>
				</td>
			</tr>
			<tr>
				<td>
					<font size=2 color="BLUE"><a href="HostedPaymentIndexHttp.jsp">Hosted
							Transaction</a>
					</font>
				</td>
			</tr>
		</table>
	</body>
</HTML>