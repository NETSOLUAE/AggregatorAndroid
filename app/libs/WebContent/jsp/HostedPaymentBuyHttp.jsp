
<%@page import="java.text.NumberFormat"%>
<%@page import="com.fss.plugin.iPayPipe"%>
<%@page import="java.util.Properties"%>
<%@page import="java.text.DecimalFormat"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
</HEAD>
</HTML>
<%@ page language="java" session="true" %>
<%@ page import="java.util.Random" %>
<%@ page import="java.io.*" %>
<%
		File basePath = null;;
		Properties properties = null;
		FileInputStream inStream = null;
		File inputFile = null;
		String resourcePath = null;
		String aliasName = null;
		String currency = null;
		String language = null;
		String receiptURL = null;
		String errorURL = null;
		String udf1 = request.getParameter("udf1");
		String udf2 = null;
		String udf4 = request.getParameter("udf4");
		String udf5 = null;
		String action = request.getParameter("action");
	 	String xmlData = null;
	 	processlabel:{
			try {
		 		properties = new Properties();
		 		basePath = new File(pageContext.getServletContext().getRealPath("/"));
		 		System.out.println("basePath:::::::"+basePath);
		 		inputFile = new File(basePath+"/jsp/properties/", "transaction.properties");
				inStream = new FileInputStream(inputFile);
				properties.load(inStream);
				resourcePath = properties.getProperty("resourcePath");
				aliasName = properties.getProperty("aliasName");
				currency = properties.getProperty("currency");
				language = properties.getProperty("language");
				receiptURL = properties.getProperty("receiptURL");
				errorURL = properties.getProperty("errorURL");
				udf2 = properties.getProperty("udf2");
				udf5 = properties.getProperty("udf5");
				//action = properties.getProperty("action");
				String price = request.getParameter("PRICE");
				double pr = Double.parseDouble(price);
				String amount = new DecimalFormat("#0.000").format(pr);
				//NumberFormat formatter = new DecimalFormat("#0.000");	
				Random rnd = new Random(System.currentTimeMillis());
				String trackid = String.valueOf(Math.abs(rnd.nextInt()));
				if(!validateParamenters(resourcePath,aliasName, currency, language, receiptURL, errorURL, udf2, udf5)){
					out.println("Please configure the required parameters in file " +inputFile.getAbsolutePath());
					break processlabel;
				}
				xmlData = properties.getProperty("xmlData");
				iPayPipe pipe = new iPayPipe();
				pipe.setResourcePath(resourcePath);
				pipe.setKeystorePath(resourcePath);
				pipe.setAlias(aliasName);
				pipe.setAction(action);
				pipe.setAmt(amount);
				pipe.setCurrency(currency);
				pipe.setLanguage(language);
				pipe.setResponseURL(receiptURL);
				pipe.setErrorURL(errorURL);
				
				pipe.setTrackId(trackid);
				pipe.setUdf1(udf1);
				pipe.setUdf2(udf2);
				//pipe.setUdf3(InetAddress.getLocalHost().getHostAddress());
				pipe.setUdf3("10.1.5.143");
				pipe.setUdf4(udf4);
				pipe.setUdf5(udf5);
				if(pipe.performPaymentInitializationHTTP()!=0){
				  	out.println("ERROR OCCURED! " + pipe.getError());
					return;
				}
				response.sendRedirect(pipe.getWebAddress());
			}catch (Exception e) {
				e.printStackTrace();
				out.println(e instanceof IOException ? "ERROR OCCURED! Error in file please check the path"+inputFile.getAbsolutePath() + e : "ERROR OCCURED! "+ e);
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

