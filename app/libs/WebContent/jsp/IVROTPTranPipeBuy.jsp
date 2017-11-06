
<%@page import="com.fss.plugin.iPayPipe"%>
<%@page import="java.io.*"%>
<%@page import="java.util.Properties"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Canon - Tranportal 3D Secure</TITLE>
</HEAD>
</HTML>
<%@ page language="java" session="true" %>
<%@ page import="java.util.Random" %>
<%
		File basePath = null;
		Properties properties = null;
		FileInputStream inStream = null;
		File inputFile = null;
		String resourcePath = null;
		String aliasName = null;
		String xmlData = null;
		iPayPipe pipe = new iPayPipe();
		try{
			properties = new Properties();
			basePath = new File(pageContext.getServletContext().getRealPath("/"));
			inputFile = new File(basePath+"/jsp/properties/", "transaction.properties");
			inStream = new FileInputStream(inputFile);
			properties.load(inStream);
		}catch(Exception e){

		}
		resourcePath = properties.getProperty("resourcePath");
		aliasName = properties.getProperty("aliasName");
		xmlData = properties.getProperty("xmlData");
		pipe.setPaymentId(request.getParameter("paymentid"));
		pipe.setOtp(request.getParameter("otp"));
		pipe.setAlias(aliasName);
		pipe.setResourcePath(resourcePath);
		pipe.setKeystorePath(resourcePath);
		pipe.setUdf1("OTP");
		int i = pipe.performIVROTPTransaction();
		%>
			<HTML>
			<BODY>
			<TABLE align=center border=1  bordercolor=black><tr><td>

			<TABLE align=center border=0  bordercolor=black>
					<TR>
						<TD colspan="2" align="center">
							<FONT size="4"><B>Transaction Details   </B></FONT>
						</TD>
					</TR>
					<TR>
						<TD colspan="2" align="center">
							<HR>
						</TD>
					</TR>
					<TR>
						<TD>Transaction Status</TD>
						<TD><b><%= pipe.getResult()%></b></TD>
					</TR>

					<TR>
						<TD> Transaction Id </TD>
						<TD><%= pipe.getTransId()%></TD>
					</TR>
					<TR>
						<TD> Reference Id </TD>
						<TD><%= pipe.getRef()%></TD>
					</TR>
					<tr>
						<td>TrackID</td>
						<td>
							<%=pipe.getTrackId()%>
						</td>
					</tr>

					<TR>	
						<TD>Amount</TD>
						<TD><%= session.getAttribute("amount")%></TD>
					</TR>

					<TR>
						<TD>AUTH</TD>
						<TD><%= pipe.getAuth()%></TD>
					</TR>

					<TR>
						<TD> UDF1 </TD>
						<TD><%= pipe.getUdf1() %></TD>
					</TR>
					<TR>
						<TD> UDF2 </TD>
						<TD><%= pipe.getUdf2() %></TD>
					</TR>
					<TR>
						<TD> UDF3 </TD>
						<TD><%= pipe.getUdf3() %></TD>
					</TR>
					<TR>
						<TD> UDF4 </TD>
						<TD><%= pipe.getUdf4() %></TD>
					</TR>
					<TR>
						<TD> UDF5 </TD>
						<TD><%= pipe.getUdf5() %></TD>
					</TR>
					<TR>
						<TD>Error Message</TD>
						<TD><%= pipe.getError_text()%></TD>
					</TR>
					</table>

			<br>
					<TABLE align=center border=1  bordercolor=black><tr><td>

			<TABLE align=center border=0  bordercolor=black>

					<TR>
						<TD colspan="2" align="center">
							<FONT size="4"><B>Customer Shipping Details    </B></FONT>
						</TD>
					</TR>
					<TR>
						<TD colspan="2" align="center">
							<HR>
						</TD>
					</TR>
				</TABLE></td></tr></table><td></tr></table>
			<br>
					<TABLE align=center><tr></tr> <tr></tr><tr></tr>
					<TR>
					<td></td>
					</tr>
				<tr><td>
				</td></tr></table>
			</BODY>
			</HTML>
