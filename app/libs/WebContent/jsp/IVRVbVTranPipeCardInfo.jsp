
<%@page import="java.util.Calendar"%><!DOCTYPE html PUBLIC "-//W3C//DTD html 4.01 Transitional//EN">

<html>
<head>
<title>Canon - Tranportal 3D Secure</title>
</head>
<link href="<%=request.getContextPath()%>/css/main.css" type=text/css rel=stylesheet>
<body class="bg">

<table width="100%" height="100%" cellpadding="1" cellspacing="1">
	
	<!--  body -->
	<tr>
		<td>
			<div align="center">
  <font size=5>Plug-in Test Tool</font>
</div>
<center>
<P>
<font size="5"><B>Transaction Details</B></font>
<form name="form1" ACTION="IVRVbVTranPipeBuy.jsp" METHOD="POST">
<table align=center border=0  bordercolor=black><tr><td>
<table align=center border=1  bordercolor=black>
	<tr>
		<td width="40%">Action Type</td>
		<td>
			<select name="action" class="select">
			<option value="1"> 1 - Purchase </option> 
			<option value="2"> 2 - Credit  </option> 
            <option value="3"> 3 - Void Purchase </option> 
	        <option value="4"> 4 - Authorization </option> 
			<option value="5"> 5 - Capture   </option> 
			<option value="6"> 6 - Void Credit </option> 
			<option value="7"> 7 - Void Capture </option> 
			<option value="8"> 8 - Inquiry </option> 
			<option value="9"> 9 - Void Authorization </option> 
			</select>
		</td>
    </tr>

   <tr>
		<td >Card Number:</td>
		<td><input size="20" type="text" name="cardNumber" value="6220189030080015991"></td>
	</tr>

	<tr>
		<td >CVV:</td>
		<td><input size="3" type="text" name="cvv" maxlength=4 value="001"></td>
	</tr> 


	<tr>
		<td >Expiry Month &amp; Year</td>
		<td>
			<select class="select" name="expmm" >
			<option value="">SELECT</option>
			<option value="1">1</option> 
			<option value="2">2</option> 
			<option value="3">3</option> 
			<option value="4">4</option> 
			<option value="5">5</option> 
			<option value="6">6</option> 
			<option value="7">7</option> 
			<option value="8">8</option> 
			<option value="9">9</option> 
			<option value="10">10</option> 
			<option value="11">11</option> 
			<option value="12" selected>12</option> 
			</select>
			&nbsp;
			<select class="select" name="expyy" >
			<option value="">SELECT</option>
			<option value="2011">2011</option> 
			<option value="2012">2012</option> 
			<option value="2013">2013</option> 
			<option value="2014">2014</option> 
			<option value="2015">2015</option> 
			<option value="2016">2016</option> 
			<option value="2017" selected>2017</option> 
			</select>
		</td></tr>
		<tr>
		<td >Cardholder's Name</td>
		<td><input size="20" type="text" name="name" value="Test"></td>
		</tr>
		<tr>
			<td >Amount</td>
			<td><input size="20" type="text" name="amount" value="3500"></td>
		</tr>
		
		<tr>
			<td width="40%">Transaction ID</td>
			<td><input size="20" type="text" name="comment"></td>
		</tr>
		<tr>
			<td width="40%">UDF1</td>
			<td>
				<select class="select" name="udf1" >
					<option value="OTP">OTP</option> 
					<option value="PKI">PKI</option> 
			</select>
			</td>
		</tr>
		<tr>
			<td width="40%">UDF2</td>
			<td><input size="20" type="text" name="udf2"></td>
		</tr>
		<tr>
			<td width="40%">UDF3</td>
			<td><input size="20" type="text" name="udf3" value="10.1.5.143"></td>
		</tr>
		<tr>
			<td width="40%">UDF4</td>
			<td><input size="20" type="text" name="udf4"></td>
		</tr>
		<tr>
			<td width="40%">UDF5</td>
			<td>
				<select class="select" name="udf5" >
			<option value="">SELECT</option>
			<option value="PaymentID">PaymentID</option> 
			<option value="TrackID">TrackID</option> 
			<option value="Test">Test</option>
			</select>
			</td>
		</tr>
		<tr>
			<td width="40%">Track ID</td>
			<td><input size="20" type="text" name="trckId" value="<%=String.valueOf(Calendar.getInstance().getTimeInMillis())%>"></td>
		</tr>
				<tr>
			<td>Select Type</td>
			<td>
				<select id="transacType" name="transacType">
					<option>--Select--</option>
					<option selected="selected" >DC</option>
					<option >C</option>
				</select>
			</td>
		</tr>
		
	</table> 
	</td></tr></table>

<table align=center border=0  bordercolor=black>

<tr>
		<td ></td>
		<td><input type="Submit" value="Buy"></td>
	</tr></table>
<P>
<input type=hidden name=type value="C" >
<input type=hidden name=currcd value="356" >
</form>
</center>
		</td>
	</tr>
	
</table>

</body>

</html>
