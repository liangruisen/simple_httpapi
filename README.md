# simple_httpapi
A Simple JAVA http client api

一个简单的Java http client 调用 api，暂时不支持文件上传

使用示例如下：

 SimpleHttp simpleHttp = SimpleHttpFactory.getSimpleHttp();
 
 String urlstring = "http://www.xxxx.com";
 
 SimpleResponse response = simpleHttp.get(urlstring);
 
 if(response.isSuccessful()){
 
 		String htmlContent = response.getString();

 }
 
 可选择使用httpclient/okhttp/HttpURLConnection的具体实现http请求