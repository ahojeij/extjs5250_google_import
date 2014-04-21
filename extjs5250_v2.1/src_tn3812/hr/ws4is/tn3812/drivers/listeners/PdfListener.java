/*
 * Copyright (C) 2014,  Tomislav Milkovic
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */
package hr.ws4is.tn3812.drivers.listeners;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import hr.ws4is.tn3812.drivers.processors.IControls;
import hr.ws4is.tn3812.drivers.processors.scs.SCSPageOrientation;
import hr.ws4is.tn3812.drivers.processors.scs.SCSStyleType;
import hr.ws4is.tn3812.interfaces.ITn3812Context;

final class PdfListener implements IProcessorListener {

	ByteBuffer buffer;
	IControls controls;
	
	BaseFont courier;
	Font font;
	Font fontBold;
	Font current;
	
	File file ;
	OutputStream fos;
	PdfWriter printWriter;
	Document document;
	boolean printing = false;
	
	public PdfListener() {
		super();
		try {
			courier = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public void onFormFeed() {
		printing = true;
	}

	@Override
	public void onLineFeed() {

	}

	@Override
	public void onHorizontalMove() {

	}

	@Override
	public void onVerticalMove() {

	}

	@Override
	public void onNewLine() {

		if(printing){
			document.newPage();
			printing = false;
		}

		byte[] data = null;
		
		try {
			
			if(buffer!=null){
				data = new byte[buffer.limit() - buffer.position()];
				buffer.get(data);
			} else {
				data = new byte[0];
			}
			
			int move = controls.getLineSize();
			String line = (new String(data,"Cp870"));
			document.add(new Paragraph(move, line, font));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (java.nio.BufferUnderflowException be){
			be.printStackTrace();
		}
	}


	@Override
	public void onFontStyleChange(SCSStyleType type) {

		if(type == SCSStyleType.BOLDING){
			current = fontBold;
		} else {
			current = font;
		}
	}

	@Override
	public void onStart(IControls controls) {

		this.controls = controls;
		
		Rectangle ps = null;
		if(controls.getPageOrientation() == SCSPageOrientation.LANDSCAPE){
			ps = PageSize.A4.rotate();
		} else {
			ps = PageSize.A4;
		}
		document = new Document(ps, 15, 0, 0, 0);
		
		font = new Font(courier, controls.getFontSize());
		fontBold = new Font(courier, controls.getFontSize());
		fontBold.setStyle(Font.BOLD);
		current = font;
		
		file = prepareFile();
		fos = open(file);
		try {
			printWriter = PdfWriter.getInstance(document, fos);
			printWriter.setViewerPreferences(PdfWriter.PrintScalingNone);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		document.open();
		
	}

	@Override
	public void onFinish(ITn3812Context context) {
		try {
			printPDF();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		//save result in context shared parameters list
		if(context!=null){
			context.setData(File.class.getCanonicalName(), file);
		}
	}

	@Override
	public void onData(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	
	private File prepareFile(){
		
		String path = System.getProperty("java.io.tmpdir").concat("/output");
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
		file = new File(file, System.nanoTime() + ".pdf");
		return file;
	}
	
	
	private void printPDF() throws DocumentException{
		printWriter.flush();
		if(document.isOpen()){
			document.close();
		}
		printWriter.close();
		close(fos);
	}
	
	private OutputStream  open(File f){
		FileOutputStream fileStream = null;		
		try {
			fileStream = new FileOutputStream(f);						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 		
		BufferedOutputStream bos = new BufferedOutputStream(fileStream);				
		return bos;
	}
	
	private void close(OutputStream stream){
		if(stream == null) return;
		try {
			stream.close();
			stream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
