package hr.ws4is.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TnReportServlet
 */
@WebServlet("/reports")
public class TnReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("r");
		if(fileName == null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		File file  = prepareFile(fileName);
		if(!file.exists()){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		OutputStream out = null;
		RandomAccessFile raf = null;
		FileChannel channel = null;
		try{
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","inline; filename='"+file.getName()+"'");
			
			out = response.getOutputStream();
			
			raf = new RandomAccessFile (file,"r");
			channel = raf.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(256 * 1024);
			while(channel.read(buffer) > 0) {
		            buffer.flip();
		            out.write(buffer.array(),0, buffer.limit());
		            buffer.clear(); 
		    }
			buffer.clear();			
		}finally{
			if(channel!=null){
				channel.close();	
			}
			if(raf!=null){
				raf.close();
			}
			out.close();
		}
		
		
	}

	
	private File prepareFile(String fileName){
		
		String path = System.getProperty("java.io.tmpdir").concat("/output");
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
		file = new File(file, fileName);
		return file;
	}
	
}
