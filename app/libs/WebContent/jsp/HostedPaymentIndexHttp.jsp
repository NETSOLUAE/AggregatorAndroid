<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.01 Transitional//EN">
<%@ page language="java" session="true"%>
<html>
<head>
<title>Canon - Hosted</title>
<meta HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<meta name="GENERATOR" content="IBM WebSphere Studio">
<link href="<%=request.getContextPath()%>/css/main.css" type=text/css rel=stylesheet>
</head>
<body class="bg">
<table width="100%" height="100%" cellpadding="1" cellspacing="1">
	<!--  body -->
	<tr valign="top">
		<td>
			<form name="form1" ACTION="HostedPaymentBuyHttp.jsp" METHOD="POST">
			<input type="hidden" name="SKU"   value="PGU-203">
			<input type="hidden" name="NAME"  value="Canon EOS 550D">
			<table border="0">
			    <tbody>
			         <tr>
			            <td colspan="7"><hr></td>
			        </tr>
			        <tr>
			            <td width="23"></td>
			            <td width="155"></td>
			            <td></td>
			            <td width="155"></td>
			            <td width="53"></td>
			            <td width="116"></td>
			            <td width="29"></td>
			        </tr>
			        <tr>
			            <td width="23"></td>
			            <td width="155" rowspan="5"><img src="<%=request.getContextPath()%>/images/550d.jpg" height="75%"></td>
			            <td></td>
			            <td width="155"><B>Canon EOS 550D</B></td>
			            <td width="53"></td>
			            <td width="116"></td>
			            <td width="29"></td>
			        </tr>
			        <tr>
			            <td width="23"></td>
			            <td></td>
			            <td colspan="3" rowspan="3">Canon has unveiled the latest in its long line of consumer digital SLRs, the Rebel T2i (EOS 550D). Highlights include 1080p HD video recording (with full manual control), an 18MP CMOS sensor, 3 inch 3:2 LCD with 1040k dot resolution and the 63-point iFCL metering system first seen on the EOS 7D.</td>
			            <td width="29"></td>
			        </tr>
			        <tr>
			            <td width="23"></td>
			            <td></td>
			            <td width="29"></td>
			        </tr>
			        <tr>
			            <td width="23"></td>
			            <td></td>
			            <td width="29"></td>
			        </tr>
			        <tr>
			            <td width="23"></td>
			            <td></td>
			            <td align="right">Price (RS):</td>
			            <td align="left"><input type="text" name="PRICE" size="8" value="500" align="right"></td>
			            <td align="right">Action</td>
			            <td>
			             <select id="action" name="action">
			            		<option value="1">1-Purchase</option>
			            		<option value="4">4-Authorization</option>
			            		<option value="8">8-Inquiry</option>
			            	</select>
			            </td>
			             <td align="right">Inquiry id (udf1)</td>
			            <td>
			             <input type="text" name="udf1" size="8" align="right">
			            </td>
			             <td align="right">Inquiry Type (udf4)</td>
			            <td>
			             	<select class="select" name="udf4" >
								<option value="">SELECT</option>
								<option value="PaymentID">PaymentID</option> 
								<option value="TrackID">TrackID</option> 
								<option value="SeqNum">SeqNum</option> 
							</select>
			            </td>
			        <tr>
			            <td width="23"></td>
			            <td width="155"></td>
			            <td></td>
			            <td align="right" width="155">Qty:</td>
			            <td align="left" width="53">1</td>
			            <td width="116"></td>
			            <td width="29"></td>
			        </tr>
			        <tr>
			            <td width="23"></td>
			            <td
			                width="155" align="right"></td>
			            <td></td>
			            <td width="155"></td>
			            <td align="left" width="53"><input TYPE="Submit" Value="Purchase"></td>
			            <td width="116"></td>
			            <td width="29"></td>
			        </tr>
			        <tr>
			            <td colspan="7"><hr></td>
			        </tr>
			    </tbody>
			</table>
			</form>
		</td>
	</tr>
</table>
</body>
</html>
