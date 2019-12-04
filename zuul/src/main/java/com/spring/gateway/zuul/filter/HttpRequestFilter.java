package com.spring.gateway.zuul.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestFilter  {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestFilter.class);


    private void httpRequestFilter(HttpServletRequest request) {
        System.out.println(request.getRequestURL().toString() + " 请求访问");
        //路由标识key  判断连接哪个服务
        String header = request.getHeader("sys");
        String time = request.getHeader("time");
        ReadableHttpServletRequestWrapper readableRequest = null;
        try {
            //验证时间戳是否超过配置时间
            if ((System.currentTimeMillis() - Long.valueOf(time)) > 3 * 60 * 1000) {
                //todo 提示接口超时  请重新发送请求
            }
            readableRequest = new ReadableHttpServletRequestWrapper(request);
            String requestBody = getBodyString(readableRequest.getReader());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public String filterType() {
        //fiterType,有pre、route、post、error四种类型，分别代表路由前、路由时
        //路由后以及异常时的过滤器
        return "post";
    }

    @Override
    public int filterOrder() {
        //排序，指定相同filterType时的执行顺序，越小越优先执行
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //是否应该调用run()方法  可以通过http参数判断是否过滤
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        httpRequestFilter(request);
        return null;
    }
*/
    /**
     * 获取请求体
     *
     * @param reader
     * @return
     */
    private String getBodyString(BufferedReader reader) {
        String inputLine;
        StringBuilder result = new StringBuilder();
        try {
            while ((inputLine = reader.readLine()) != null) {
                result.append(inputLine);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IOException: " + e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("IOException: " + e);
            }
        }
        return result.toString();
    }


    //数据加密  根据hader时间戳设置加解密密钥
    private void key() {

    }
}
