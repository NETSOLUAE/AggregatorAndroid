<%@page import="javax.sound.midi.SysexMessage"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.net.InetAddress"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.IOException"%>
<%@page import="com.fss.plugin.iPayPipe"%><!DOCTYPE html PUBLIC "-//W3C//DTD html 4.01 Transitional//EN"> 
<html>
<head>
<meta name="GENERATOR" content="IBM WebSphere Studio">
<link href="<%=request.getContextPath()%>/css/main.css" type=text/css rel=stylesheet>
<title>Canon - Tranportal</title>
</head>
<%@ page language="java" session="true" %>
<%! 
	public boolean validateParamenters(String... val){
		try{
			for(String v : val){
				if(v != null && !v.trim().isEmpty() && !("null").equals(v.trim())){
					continue;
				}else{
					return false;
				}
			}
		}catch(Exception ex){
			return false;
		}
		return true;
	}

%>
<%
		File basePath = null;
		Properties properties = null;
		FileInputStream inStream = null;
		File inputFile = null;
		String resourcePath = null;
		String aliasName = null;
		String currency = null;
		String language = null;
		String receiptURL = null;
		String errorURL = null;
		String udf1 = null;
		String udf2 = null;
		String udf3 = null;
		String udf4 = null;
		String udf5 = null;
		String action = null;
	 	String xmlData = null;
	 	String comment = null;
		String amount = null;
		String cardNumber    = null;
		String cvv    = null;
		String expmm  = null;
		String expyy  = null;
		String Type  = null;
		String name = null;
		String trckId = null;
		iPayPipe pipe = null;
		String paymenturl= null;
	 	processlabel:{
			try {
				pipe = new iPayPipe();
		 		properties = new Properties();
		 		basePath = new File(pageContext.getServletContext().getRealPath("/"));
		 		inputFile = new File(basePath+"/jsp/properties/", "transaction.properties");
				inStream = new FileInputStream(inputFile);
				properties.load(inStream);
				resourcePath = properties.getProperty("resourcePath");
				aliasName = properties.getProperty("aliasName");
				language = properties.getProperty("language");
				currency = properties.getProperty("currency");
				receiptURL = properties.getProperty("tranReceiptURL");
				errorURL = properties.getProperty("tranErrorURL");
				xmlData = properties.getProperty("xmlData");
				if(!validateParamenters(resourcePath, aliasName, language, currency)){
					out.println("Please check the parameters in the file "+inputFile.getAbsolutePath());
					return;
				}
				comment = request.getParameter("comment");
				action = request.getParameter("action");
				udf1 = request.getParameter("udf1");
				udf2 = request.getParameter("udf2");
				udf3 = request.getParameter("udf3");
				udf4 = request.getParameter("udf4");
				udf5 = request.getParameter("udf5");
				amount = request.getParameter("amount");
				cardNumber    = request.getParameter("cardNumber");
				cvv    = request.getParameter("cvv");
				expmm  = request.getParameter("expmm");
				expyy  = request.getParameter("expyy");
				Type  = request.getParameter("transacType");
				name = request.getParameter("name");
				trckId = request.getParameter("trckId");
				pipe.setTrackId(trckId);
				pipe.setAlias(aliasName);
				pipe.setResourcePath(resourcePath);
				pipe.setAction(action);
				pipe.setAmt(amount);
				pipe.setCurrency(currency);
				pipe.setCard(cardNumber);
				pipe.setCvv2(cvv);
				pipe.setExpMonth(expmm);
				pipe.setExpYear(expyy);
				pipe.setMember(name);
				pipe.setType(Type);
				pipe.setLanguage(language);
				pipe.setTransId(comment);
				pipe.setUdf1(udf1);
				pipe.setUdf2(udf2);
				//pipe.setUdf3(InetAddress.getLocalHost().getHostAddress());
				pipe.setUdf3(udf3);
				pipe.setUdf4(udf4);
				pipe.setUdf5(udf5);
				pipe.setKeystorePath(resourcePath);
				pipe.setResponseURL(receiptURL);
				pipe.setErrorURL(errorURL);
				if(pipe.performTransaction() == -1){
					break processlabel;
				}else if(pipe.getPaymentId()!=null && pipe.getPaymentUrl()!=null
					 && !pipe.getPaymentId().trim().isEmpty()
				 	 && !pipe.getPaymentUrl().trim().isEmpty()){
					paymenturl=(properties.getProperty("paymenturl") != null ? properties.getProperty("paymenturl") : pipe.getPaymentUrl())+"?PaymentID="+pipe.getPaymentId();
					response.sendRedirect(paymenturl);
					return;
				}else{
					break processlabel;
				}
			}catch(Exception e){
				out.println(e instanceof IOException ? "ERROR OCCURED! Error in file please check the path"+inputFile.getAbsolutePath() + e : "ERROR OCCURED! "+ e);
				return;
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
	}
%>
<head>
</head>
<body>
<table align=center border=1  bordercolor=black>
	<tr>
		<td>
			<table align=center border=0  bordercolor=black>
				<tr>
					<td colspan="2" align="center">
						<font size="4"><B>Transaction Details   </B></font>
					</td>
				</tr>
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
						&nbsp;&nbsp;<%=pipe.getDate()%>
					</td>
				</tr>
				<tr>
					<td>
						Transaction Reference ID
					</td>
					<td>
						&nbsp;&nbsp;<%=pipe.getRef()%>
					</td>
				</tr>
				<tr>
					<td>
						Mrch Track ID
					</td>
					<td>
						&nbsp;&nbsp;<%=pipe.getTrackId()%>
					</td>
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
						&nbsp;&nbsp;<%=pipe.getAmt()%>
					</td>	
				</tr>
				<tr>
					<td>
						UDF5
					</td>
					<td>
						&nbsp;&nbsp;<%=pipe.getUdf5()%>
					</td>
				</tr>
				<tr>
					<td>
						Payment ID
					</td>
					<td>
						&nbsp;&nbsp;<%=pipe.getPaymentId()%>
					</td>
				</tr>
				<tr>
					<td>
						ErrorText
					</td>
					<td>
						&nbsp;&nbsp;<%=request.getParameter("ErrorText")%>
					</td>
				</tr>
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
