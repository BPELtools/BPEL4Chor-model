package org.bpel4chor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Utility for reading or writing ini File [Project_Path]\conig\bpel4chor.ini .
 * 
 * <p>
 * It provides Input/OutputStream to the ini file.
 * 
 * @created Oct 5, 2011
 * @author Daojun Cui
 * @deprecated
 */
public class BPEL4ChorIniFileUtil {
	
	/**
	 * Get the bpel4chor.ini file InputStream
	 * 
	 * <p>
	 * both the eclipse way and the non-eclipse way will be used to get the file input stream.
	 *  
	 * @return inputStream
	 */
	public static InputStream getIniFileInputSteam()
	{
		InputStream in = null;
	
		try {
			// try eclipse way first
			in = getIniFileInputStreamEclipse();
		} catch (ExceptionInInitializerError e) {
			
			// uncomment this line to see the error stack
			// e.printStackTrace(); 

			// if eclipse is not running, then try non-eclipse way.
			System.out.println("Eclipse FileLocator failed to locate the ini File, now try non-eclipse way");
			in = getInitFileInputStreamNonEclipse();
		}
		
		return in;
	}
	
	/**
	 * Get the bpel4chor.ini file InputStream, the File Path is provided from eclipse bundle.
	 * <p>
	 * <b>Note</b>: This method works only with eclipse runtime dependencies
	 * @return inputStream
	 */
	public static InputStream getIniFileInputStreamEclipse()
	{
		InputStream in = null;
		Bundle bundle = Platform.getBundle("org.bpel4chor.model");// bundle-ID in plug-in-descriptor
		Path path = new Path("config/bpel4chor.ini");
		URL fileURL = FileLocator.find(bundle, path, null);
		
		try {
			if (fileURL != null) {
				URL absoluteUrl = Platform.resolve(fileURL);
				//System.out.println("Get InputStream: "+new File(absoluteUrl.getFile()).getAbsolutePath());
				in = absoluteUrl.openStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return in;
	}
	
	/**
	 * Get the bpel4chor.ini file InputStream, non-eclipse, command line.
	 * <p>
	 * @return inputstream of ini property file
	 */
	public static FileInputStream getInitFileInputStreamNonEclipse(){
		
		FileInputStream in = null;
		
		String projDir = getProjectDirNonEclipse();
		String pathToIniFile = projDir +File.separator+"config"+File.separator+"bpel4chor.ini";
		
		try {
			
			File file = new File(pathToIniFile);
			if(file.exists()) 
				in = new FileInputStream(file);
			else
				in = new FileInputStream(new File(projDir, pathToIniFile));
			
		} catch (FileNotFoundException e) {
			System.out.println("Can not find the property file in location: " + projDir + File.separator + pathToIniFile + ". " 
				+ e.toString());
		} 
		return in;
	}
	
	/**
	 * Get the ini property file FileOutputStream
	 * <p>
	 * <b>Note</b>: This method works only for non-eclipse command-line style
	 * @return outputstream of ini property file
	 */
	public static FileOutputStream getInitFileOutputStreamNonEclipse(){
		
		FileOutputStream out = null;
		
		String projectDir = getProjectDirNonEclipse();
		String path = File.separator+"config"+File.separator+"bpel4chor.ini";
		File file = new File(projectDir, path);
		try {
			out = new FileOutputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Get the project current absolute path
	 * 
	 * <b>Note</b>: This method works only for non-eclipse command-line style
	 * 
	 * @return project current directory
	 */
	public static String getProjectDirNonEclipse(){
	   
		File file = new File("");
		String prjDir = file.getAbsolutePath();
		return prjDir;
	}
}
