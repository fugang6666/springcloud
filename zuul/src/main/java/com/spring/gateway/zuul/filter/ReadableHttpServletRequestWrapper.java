package com.spring.gateway.zuul.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class ReadableHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public ReadableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = readBytes(request.getInputStream(), request.getContentLength());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(),"UTF-8"));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    public void setBody(byte[] bytes)
    {
        this.body = bytes.clone();
    }

    private byte[] readBytes(InputStream inputStream,long contentLength)
    {
        byte[] bytes = new byte[(int)contentLength];
        byte[] buffers = new byte[1024];
        int totalcount=0;
        int readcount = 0;
        try {
            while((readcount= inputStream.read(buffers))>0)
            {
                System.arraycopy(buffers,0,bytes,totalcount,readcount);
                totalcount+=readcount;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
