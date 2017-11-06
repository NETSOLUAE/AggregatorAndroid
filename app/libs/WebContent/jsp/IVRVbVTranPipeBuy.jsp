
<%@page import="com.fss.plugin.iPayPipe"%>
<%@page import="java.io.*"%>
<%@page import="java.util.Properties"%><!DOCTYPE html PUBLIC "-//W3C//DTD html 4.01 Transitional//EN">
<html>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>Canon - Tranportal 3D Secure</TITLE>
</HEAD>
</html>
<%@ page language="java" session="true" %>
<%@ page import="java.util.Random" %>
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
		String udf1 = null;
		String udf2 = null;
		String udf4 = null;
		String udf5 = null;
		String action = null;
		String amount = null;
		String cardNumber    = null;
		String cvv    = null;
		String expmm  = null;
		String expyy  = null;
		String Type  = null;
		String name = null;
		String trckId = null;
		iPayPipe pipe = null;
	 	String PARes = null;
	 	Random rnd = null;
	 	processlabel:{
			try {
				rnd = new Random(System.currentTimeMillis());
				PARes = request.getParameter("PaRes");
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
				if(!validateParamenters(resourcePath, aliasName, language, currency)){
					out.println("Please check the parameters in the file "+inputFile.getAbsolutePath());
					return;
				}
				action = request.getParameter("action");
				udf1 = request.getParameter("udf1");
				udf2 = request.getParameter("udf2");
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
				if(PARes == null) {
					%>Processing...<%	
					pipe.setAlias(aliasName);
					pipe.setResourcePath(resourcePath);
					pipe.setKeystorePath(resourcePath);
					pipe.setAmt(amount);
					session.setAttribute("amount",amount);
					pipe.setCurrency(currency);
					pipe.setMember(name);
					pipe.setAction(action);
					pipe.setTrackId(String.valueOf(Math.abs(rnd.nextLong())));
					pipe.setCvv2(cvv);
					pipe.setExpMonth(expmm);
					pipe.setExpYear(expyy);
					pipe.setExpDay(expmm);  
					pipe.setCard(cardNumber);
					pipe.setUdf1(udf1);
					pipe.setUdf2(udf2);
					pipe.setUdf4(udf4);
					pipe.setUdf5(udf5);	
					pipe.setIvrFlag("IVR");
					//pipe.setNpc356availauthchannel("SMS");
					//pipe.setNpc356chphoneid("23232323");
					//pipe.setNpc356chphoneidformat("D");
					//pipe.setNpc356itpcredential("erer454rerer34ere5");
					//pipe.setNpc356pareqchannel("DIRECT");
					//pipe.setNpc356shopchannel("IVR");	
					pipe.setType(request.getParameter("transacType"));
					pipe.performIVRVETransaction();
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
			if(pipe.getResult() != null) {	
				if(pipe.getResult().equals("pass")) {		
					if(pipe.getUdf1()!=null && pipe.getUdf1().equals("OTP")){%>
						<html>
							<BODY class="bg">
								<br>
								<form method="post" action="IVROTPTranPipeBuy.jsp">
									<TABLE align=center border=1  bordercolor=black>
										<tr>
											<td>
												<TABLE align=center border=0  bordercolor=black>
													<TR>
														<TD colspan="2" align="center">
															<FONT size="4"><B>OTP</B></FONT>
														</TD>
														<TD colspan="2" align="center">
															<input type="text" name="otp" id="otp" />
														</TD>
														<TD colspan="2" align="center">
															<input type="hidden" name="paymentid" id="paymentid" value="<%=pipe.getPaymentId() %>" />
															<input type="submit" value="submit" />
														</TD>
													</TR>
												</TABLE>
											</td>
										</tr>
									</TABLE>
								</form>
							</BODY>
						</html>
						<%} else {%>
							<script>
								alert("Invalid udf value.");
							</script>
						<%}
				} else {%>
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
				<%	}
				}	
			} %>