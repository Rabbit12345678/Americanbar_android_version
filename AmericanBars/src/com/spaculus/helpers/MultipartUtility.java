package com.spaculus.helpers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
 
/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 * @author www.codejava.net
 *
 */
public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private DataOutputStream outputStream = null;
    private PrintWriter writer;
 
    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset)
            throws IOException {
        this.charset = charset;
         
        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
         
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
       // httpConn.setChunkedStreamingMode(256 * 1024);

        // Allow Inputs & Outputs  
        httpConn.setDoInput(true);  
        httpConn.setDoOutput(true);  
        httpConn.setUseCaches(false);  

        // Enable POST method  
        httpConn.setRequestMethod("POST");  
        httpConn.setRequestProperty("Connection", "Keep-Alive");  
        //httpConn.setRequestProperty("Transfer-Encoding","chunked");
        httpConn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);  
        
        outputStream = new DataOutputStream(httpConn.getOutputStream());  
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }
 
    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }
 
    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: "+ URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
 
        FileInputStream inputStream = new FileInputStream(uploadFile);
        
        int bytesRead;
        int bytesAvailable, bufferSize;  
        byte[] buffer;  
        int maxBufferSize = 256 * 1024;// 256KB  
        bytesAvailable = inputStream.available();  
        bufferSize = Math.min(bytesAvailable, maxBufferSize);  
        buffer = new byte[bufferSize];  
        
        bytesRead = inputStream.read(buffer, 0, bufferSize);
        while (bytesRead > 0) {  
            outputStream.write(buffer, 0, bufferSize);  
            bytesAvailable = inputStream.available();  
            bufferSize = Math.min(bytesAvailable, maxBufferSize);  
            bytesRead = inputStream.read(buffer, 0, bufferSize);  
        }
        inputStream.close();
        outputStream.flush();
        writer.append(LINE_FEED);
        writer.flush();    
    }
    
	/* For the Add/Edit Album */
    public void addFilePart(String fieldName, File uploadFile, String fileName, boolean isLocalObject) throws IOException {
        
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: "+ URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
 
        if(isLocalObject) {
        	 FileInputStream inputStream = new FileInputStream(uploadFile);
             
             int bytesRead;
             int bytesAvailable, bufferSize;  
             byte[] buffer;  
             int maxBufferSize = 256 * 1024;// 256KB  
             bytesAvailable = inputStream.available();  
             bufferSize = Math.min(bytesAvailable, maxBufferSize);  
             buffer = new byte[bufferSize];  
             
             bytesRead = inputStream.read(buffer, 0, bufferSize);
             while (bytesRead > 0) {  
                 outputStream.write(buffer, 0, bufferSize);  
                 bytesAvailable = inputStream.available();  
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);  
                 bytesRead = inputStream.read(buffer, 0, bufferSize);  
             }
             inputStream.close();
             outputStream.flush();
        }
        
        writer.append(LINE_FEED);
        writer.flush();    
    }
    
    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }
     
    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();
 
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
 
        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
 
        return response;
    }
}
