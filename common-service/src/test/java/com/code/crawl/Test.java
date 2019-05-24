package com.code.crawl;

import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxyplugin.JiGuangProxyPlugin;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

public class Test {
    @org.junit.Test
    public void testt() throws IOException, ClassNotFoundException, URISyntaxException {

        WebRequest req = new WebRequest("https://www.baidu.com/?tn=monline_3_dg");
        req.setCookie("BAIDUID=C19546E298915105CC0C6518070EE3B7:FG=1; BIDUPSID=F84824E997CC27748B441FEC160A917B; PSTM=1551428467; BD_UPN=133252; BDUSS=GxEcEgxOENjV250T1RCdVlyc35WMC1SR1VqM21tTkljblpEOHRwUVUzQks0TlJjSVFBQUFBJCQAAAAAAAAAAAEAAADnsmKWzOzM7MvRy~cyMDE3AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEpTrVxKU61cb; MCITY=-179%3A; BDORZ=FFFB88E999055A3F8A630C64834BD6D0; H_PS_PSSID=1466_28938_21121_28519_28774_28724_28964_28836_28584_22157; delPer=0; BD_CK_SAM=1; PSINO=5; BDRCVFR[Fc9oatPmwxn]=mk3SLVN4HKm; BD_HOME=1; sug=3; sugstore=1; ORIGIN=0; bdime=0");
        WebClient client = WebClient.buildDefaultClient();

        WebResponse resp = client.execute(req);

        System.out.println(resp.getCookie());
        System.out.println(client.getClientCookies(req, resp));


//        String s = "auto_login=9b398806f11fc7bf20d5b3e3ead90852; expires=Fri, 17-May-2019 15:53:19 GMT; Max-Age=604799; path=/";
//
//        System.out.println(JSONObject.parseObject(s, Cookie.class));


//        System.out.println(RedisUtils.getValueByKey("test1"));
//        for(Class c: ProxyUtils.getPluginClasses()){
//            System.out.println(c.getName());
//        }

//        System.out.println(RedisUtils.buildConnection() == null);
//        System.out.println(RedisUtils.redisTemplate().opsForValue().get("test1"));

//        List<Class> classes = new ArrayList<>();
//        String packageName = "com/code/common/proxyplugin/";
//
//        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
//        URL packUrl = classLoader.getResource(packageName);
//        URL[] urls = new URL[]{packUrl};
//        classLoader = new URLClassLoader(urls);
//
//        for (File file : new File(packUrl.getPath()).listFiles()) {
//            String className = packageName.replace("/", ".") + StringUtils.substring(file.getName(),0,  file.getName().lastIndexOf("."));
//            Class cls = classLoader.loadClass(className);
//            classes.add(cls);
//        }


//        URL url = ProxyUtils.class.getClassLoader().getResource("com/code/common");
//        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
//        URL url = classLoader.getResource("com/code/common/proxyplugin/");
//        File file = new File(url.getPath());
//
//        for (File f : file.listFiles()) {
//            System.out.println(f.getPath());
////            System.out.println(Class.forName("QiYunProxyPlugin.class"));
////            Class cls = classLoader.loadClass(f.getName());
////            System.out.println(cls.getSimpleName());
//        }
//        URL[] uris = new URL[1];
//        uris[0] = new URL("file:" + url.getPath());
//
//        Class cls = new URLClassLoader(uris).loadClass("com.code.common.proxy.ProxyPlugin");
//        System.out.println(cls.getSimpleName());

//        Enumeration<URL> urls = classLoader.getResources("com/code/common/proxyplugin/");
//
//        while(urls.hasMoreElements()){
//            URL[] uris = new URL[1];
//            uris[0] = urls.nextElement();
//
////            System.out.println(s);
//            Class cls = new URLClassLoader(uris).
//            System.out.println(cls.getSimpleName());
//        }

//        while(uris.hasMoreElements()){
//            System.out.println(uris.nextElement().getPath());
//        }
//        System.out.println(ResourceUtils.getFile("/src/main/java/com.code.common/proxyplugin")==null);
    }

    @org.junit.Test
    public void test1() throws IOException, URISyntaxException {

//        PreInterceptor p1 = new PreInterceptor() {
//
//            @Override
//            public void executeTask(HttpRequest httpRequest, HttpClientContext httpContext) {
//                System.out.println("pre1 task");
//            }
//        };
//        PreInterceptor p2 = new PreInterceptor() {
//            @Override
//            public void executeTask(HttpRequest httpRequest, HttpClientContext httpContext) {
//                System.out.println("pre2 task");
//            }
//        };
//        p1.addNext(p2);
//        PostInterceptor pp1 = new PostInterceptor() {
//            @Override
//            public void executeTask(HttpResponse httpResponse, HttpClientContext httpContext) {
//                System.out.println("post1 task");
//            }
//        };
//        PostInterceptor pp2 = new PostInterceptor() {
//            @Override
//            public void executeTask(HttpResponse httpResponse, HttpClientContext httpContext) {
//                System.out.println("post2 task");
//            }
//        };
//        pp1.addNext(pp2);
//        WebClient webClient = WebClient.create();
//        webClient.buildPreInterceptor(p1);
//        webClient.buildPostInterceptor(pp1);
//        webClient.build();
//        webClient.execute(null);


//        WebClient client = WebUtils.multiThreadClient().build();
//        try {
//            TimeUnit.SECONDS.sleep(12);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        client.close();
//        System.out.println("close");
//
//        try {
//            TimeUnit.MINUTES.sleep(30);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        System.out.println(new Date(1557333766373L));
//        System.out.println(StringUtils.isEmpty(new JiGuangProxyPlugin().genCookie(null)));
//        String ss = "http://qhm1.cnzz.com/heatmap.gif?id=1273135583&x=1006&y=50&w=1171&s=1440x900&b=firefox&c=1&r=&a=1&random=Thu May 09 2019 00:42:53 GMT+0800 (中国标准时间)";
////        System.out.println(url.toURI().toString());
//        String s = URLEncoder.encode(ss, Charset.defaultCharset().toString());
//        s = "http://qhm1.cnzz.com/heatmap.gif?id=1273135583&x=1006&y=50&w=1171&s=1440x900&b=firefox&c=1&r=&a=1&random=Thu%20May%2009%202019%2000:42:53%20GMT+0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
//        System.out.println(WebUtils.defaultClient().execute(new WebRequest(s)).getRespText());
    }

    @org.junit.Test
    public void tet() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//将最大总连接数增加到200
        cm.setMaxTotal(200);
//将每条路由的默认最大连接数增加到20
        cm.setDefaultMaxPerRoute(20);
//增加localhost的最大连接数：80到50
        HttpHost localhost = new HttpHost("www.baidu.com");
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        System.out.println(httpClient.getConnectionManager().getClass());
    }

    //@org.junit.Test
    public void tet2() {
        Properties p = System.getProperties();

        for (Map.Entry<Object, Object> entry : p.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    //@org.junit.Test
    public void tet1() throws IOException {
        WebClient client = WebClient.buildDefaultClient();
        client.execute(null);
    }
}
