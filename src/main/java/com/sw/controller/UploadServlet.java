package com.sw.controller;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024,	// 2MB
maxFileSize=1024*1024*10,		// 10MB
maxRequestSize=1024*1024*20)	// 20MB
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String SAVE_DIR = "../../webapps/uploadFiles";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String fName = request.getParameter("fname");
		// gets absolute path of the web application
		String appPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		String savePath = appPath + SAVE_DIR;
		System.out.println("appPath : "+appPath);
		System.out.println("separator : "+File.separator);
		System.out.println("SAVE_DIR : "+SAVE_DIR);
		System.out.println("savePath : "+savePath);
		// creates the save directory if it does not exists
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		for (Part part : request.getParts()) {
			// gets absolute path of the web application
			//String fileName  = extractFileName(part);
			String tempFileName = extractFileName(part);
			System.out.println("tempFileName : "+tempFileName);
			if(tempFileName==null) {
				continue;
			}
			String fileName =genSaveFileName(tempFileName);
			//--------------------------
			System.out.println("fileName : "+fileName);
			// refines the fileName in case it is an absolute path
			fileName = new File(fileName).getName();
			part.write(savePath + File.separator + fileName);
			//part.write(fileName);
		}
		HttpSession session = request.getSession();

		session.setAttribute("message", "Upload has been done successfully!");
		response.sendRedirect("upload_end.jsp");
	}

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			System.out.println("s : "+s);
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length()-1);
				
			}
		}
		return null;
	}
	
   private String genSaveFileName(String extName) {
	    String fileName = "";
	     
	    Calendar calendar = Calendar.getInstance();
	    fileName += calendar.get(Calendar.YEAR);
	    fileName += calendar.get(Calendar.MONTH);
	    fileName += calendar.get(Calendar.DATE);
	    fileName += calendar.get(Calendar.HOUR);
	    fileName += calendar.get(Calendar.MINUTE);
	    fileName += calendar.get(Calendar.SECOND);
	    fileName += calendar.get(Calendar.MILLISECOND);
	    fileName += extName;
	    System.out.println("genSaveFileName() : "+fileName); 
	    return fileName;
	  }


}
