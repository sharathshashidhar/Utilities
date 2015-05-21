package com.servlet.actions.drs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DRSActionDom extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		String result = null, error = null;
		PrintWriter out = response.getWriter();
		System.out.println("In DRS XML Generation");
		try {
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement( "Testcase" )
					.addAttribute( "Id", request.getParameter("testcaseId") )
					.addAttribute( "Name", request.getParameter("testcaseName") );

			Element taskName = root.addElement("TaskName").addText(request.getParameter("taskName"));
			Element taskDesc = root.addElement("TaskDescription").addText(request.getParameter("taskDesc"));
			//Source Information
			Element source = root.addElement("Source");
			Element connName = source.addElement("ConnectionName")
					.addAttribute("Type", request.getParameter("selectConnType"))
					.addText(request.getParameter("srcConName"));
			Element queryAll = source.addElement("QueryAll").addText(request.getParameter("queryAllRadio"));
			Element errorProcessing = source.addElement("ErrorProcessing").addText(request.getParameter("errorProcRadio"));
			//Object List
			if(request.getParameter("allObjs") != null){
				if(request.getParameter("allObjs").contains("allObjects")){
					//All Objects Checkbox is selected
					Element allObjs = source.addElement("ObjectList").addElement("AllObjects");
				}
			}
			else {
				//Else - parse the list of objects
				String objList = request.getParameter("objList");
				Element objects = source.addElement("ObjectList");
				for (String objs: objList.split(",")){
					objects.addElement("ObjectName")
					.addText(objs.trim());
				}
			}

			Element techName = root.addElement("TechnicalName").addText(request.getParameter("techNamesRadio"));
			//Target Infomation
			Element target = root.addElement("Target");
			Element tgtConName = target.addElement("ConnectionName")
					.addText(request.getParameter("tgtConName"));
			Element tgtPrefix = target.addElement("TargetPrefix")
					.addText(request.getParameter("tgtPrefix"));
			Element loadType = target.addElement("LoadType")
					.addText(request.getParameter("loadTypeRadio"));
			Element delOption = target.addElement("DeleteOption")
					.addText(request.getParameter("deleteOptionRadio"));

			//Exclude Field
			Element excludeFields = root.addElement("ExcludeFields");




			//Set the Out with the XML as String
			StringWriter sw = new StringWriter();  
			OutputFormat format = OutputFormat.createPrettyPrint();  
			XMLWriter xw = new XMLWriter(sw, format);  
			xw.write(document);  
			result = sw.toString();
			System.out.println(result);
		} catch(Exception e){
			e.printStackTrace();
			error = e.getMessage();
		}
		if(result != null)
			out.write(result);
		else
			out.write(error);

	}


}
