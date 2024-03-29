package com.doone.CustomCollection;
import android.app.Application;
import android.webkit.WebSettings;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ZCApplication extends Application {
    public static OkHttpClient okhttp;
    public static String UserAgent;
//    Interceptor interceptor=new Interceptor() {//拦截器
//        @Override
//        public Response intercept(Chain chain) throws IOException { //拦截器
//            Request request = chain.request();
//            Headers head = request.headers();//请求头
//            HttpUrl url = request.url();//请求url
//            Response  response = chain.proceed(request);
//            Headers heads = response.headers();//返回的请求头
//            return response;
//        }
//    };
    @Override
    public void onCreate() {
        super.onCreate();
        okhttp = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)//设置超时时间 （秒）
                .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(20, TimeUnit.SECONDS)//设置写入超时时间
                .cookieJar(new CookieMangme(this))//全局cookie管理器
//                .addInterceptor(interceptor)//Application拦截器
//                .addNetworkInterceptor(interceptor)//Network拦截器
                .build();
        try {
            String userAgent = WebSettings.getDefaultUserAgent(this);
            if (userAgent == null) userAgent = System.getProperty("http.agent");
            UserAgent = URLEncoder.encode(userAgent, "utf-8");//防止包含中文
        } catch (Exception e) {
        }
    }


    /**
     * @param Url     请求地址
     * @param dataMap 上传的参数和上传的文件
    * @param callback 回调对象
     */

    public static void RequestNetwork(String Url, Map<String, Object> dataMap,Callback callback) {
        Request.Builder RBuilder = new Request.Builder().addHeader("User-Agent", UserAgent).url(Url);
        if (dataMap != null) {
            MultipartBody.Builder filePackage = null;//上传文件包
            FormBody.Builder paramPackage = null;//上传参数包
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {//  所有参数都在这里
                Object object = entry.getValue();
                if (object instanceof File) {
                    if (filePackage == null) {
                        filePackage = new MultipartBody.Builder();//文件包
                        filePackage.setType(MultipartBody.FORM); //设置类型
                    }
                    File file = (File) object;
                    String fileName = file.getName();
                    RequestBody requestBody = RequestBody.create(file, MediaType.parse(mediaType.get(fileName.replaceAll("^[^.]+",""))));
                    filePackage.addFormDataPart(entry.getKey(), fileName, requestBody);//如果key相同不会复盖，会生成list,
                } else {
                    if (paramPackage == null) paramPackage = new FormBody.Builder();//参数包
                    paramPackage.add(entry.getKey(), object.toString());//如果key相同不会复盖，会生成list,
                }
            }
            if (filePackage != null) RBuilder.post(filePackage.build());//添加文件包
            if (paramPackage != null) RBuilder.post(paramPackage.build());//添加参数包
        }
        okhttp.newCall(RBuilder.build()).enqueue(callback);
    }

    public static HashMap<String, String> mediaType = new HashMap<String, String>() {{
        put("","application/octet-stream");
        put(".xls", "application/msexcel");
        put(".rtf", "application/x-rtf");
        put(".ps", "application/postscript");
        put(".ppt", "application/x-ppt");
        put(".vsd", "application/x-vsd");
        put(".vst", "application/x-vst");
        put(".mdb", "application/x-mdb");
        put(".eps", "application/postscript");
        put(".dwf", "application/x-dwf");
        put(".tif", "image/tiff");
        put(".ico", "image/x-icon");
        put(".jpg", "image/jpeg");
        put(".png", "image/png");
        put(".pdf", "application/pdf");
        put(".001", "application/x-001");
        put(".301", "application/x-301");
        put(".323", "text/h323");
        put(".906", "application/x-906");
        put(".907", "drawing/907");
        put(".a11", "application/x-a11");
        put(".acp", "audio/x-mei-aac");
        put(".ai", "application/postscript");
        put(".aif", "audio/aiff");
        put(".aifc", "audio/aiff");
        put(".aiff", "audio/aiff");
        put(".anv", "application/x-anv");
        put(".asa", "text/asa");
        put(".asf", "video/x-ms-asf");
        put(".asp", "text/asp");
        put(".asx", "video/x-ms-asf");
        put(".au", "audio/basic");
        put(".avi", "video/avi");
        put(".awf", "application/vnd.adobe.workflow");
        put(".biz", "text/xml");
        put(".bmp", "application/x-bmp");
        put(".bot", "application/x-bot");
        put(".c4t", "application/x-c4t");
        put(".c90", "application/x-c90");
        put(".cal", "application/x-cals");
        put(".cat", "application/vnd.ms-pki.seccat");
        put(".cdf", "application/x-netcdf");
        put(".cdr", "application/x-cdr");
        put(".cel", "application/x-cel");
        put(".cer", "application/x-x509-ca-cert");
        put(".cg4", "application/x-g4");
        put(".cgm", "application/x-cgm");
        put(".cit", "application/x-cit");
        put(".class", "java/*");
        put(".cml", "text/xml");
        put(".cmp", "application/x-cmp");
        put(".cmx", "application/x-cmx");
        put(".cot", "application/x-cot");
        put(".crl", "application/pkix-crl");
        put(".crt", "application/x-x509-ca-cert");
        put(".csi", "application/x-csi");
        put(".css", "text/css");
        put(".cut", "application/x-cut");
        put(".dbf", "application/x-dbf");
        put(".dbm", "application/x-dbm");
        put(".dbx", "application/x-dbx");
        put(".dcd", "text/xml");
        put(".dcx", "application/x-dcx");
        put(".der", "application/x-x509-ca-cert");
        put(".dgn", "application/x-dgn");
        put(".dib", "application/x-dib");
        put(".dll", "application/x-msdownload");
        put(".doc", "application/msword");
        put(".dot", "application/msword");
        put(".drw", "application/x-drw");
        put(".dtd", "text/xml");
        put(".dwg", "application/x-dwg");
        put(".dxb", "application/x-dxb");
        put(".dxf", "application/x-dxf");
        put(".edn", "application/vnd.adobe.edn");
        put(".emf", "application/x-emf");
        put(".eml", "message/rfc822");
        put(".ent", "text/xml");
        put(".epi", "application/x-epi");
        put(".etd", "application/x-ebx");
        put(".exe", "application/x-msdownload");
        put(".fax", "image/fax");
        put(".fdf", "application/vnd.fdf");
        put(".fif", "application/fractals");
        put(".fo", "text/xml");
        put(".frm", "application/x-frm");
        put(".g4", "application/x-g4");
        put(".gbr", "application/x-gbr");
        put(".", "application/x-");
        put(".gif", "image/gif");
        put(".gl2", "application/x-gl2");
        put(".gp4", "application/x-gp4");
        put(".hgl", "application/x-hgl");
        put(".hmr", "application/x-hmr");
        put(".hpg", "application/x-hpgl");
        put(".hpl", "application/x-hpl");
        put(".hqx", "application/mac-binhex40");
        put(".hrf", "application/x-hrf");
        put(".hta", "application/hta");
        put(".htc", "text/x-component");
        put(".htm", "text/html");
        put(".html", "text/html");
        put(".htt", "text/webviewhtml");
        put(".htx", "text/html");
        put(".icb", "application/x-icb");
        put(".iff", "application/x-iff");
        put(".ig4", "application/x-g4");
        put(".igs", "application/x-igs");
        put(".iii", "application/x-iphone");
        put(".img", "application/x-img");
        put(".ins", "application/x-internet-signup");
        put(".isp", "application/x-internet-signup");
        put(".IVF", "video/x-ivf");
        put(".java", "java/*");
        put(".jfif", "image/jpeg");
        put(".jpeg", "image/jpeg");
        put(".js", "application/x-javascript");
        put(".jsp", "text/html");
        put(".la1", "audio/x-liquid-file");
        put(".lar", "application/x-laplayer-reg");
        put(".latex", "application/x-latex");
        put(".lavs", "audio/x-liquid-secure");
        put(".lbm", "application/x-lbm");
        put(".lmsff", "audio/x-la-lms");
        put(".ls", "application/x-javascript");
        put(".ltr", "application/x-ltr");
        put(".m1v", "video/x-mpeg");
        put(".m2v", "video/x-mpeg");
        put(".m3u", "audio/mpegurl");
        put(".m4e", "video/mpeg4");
        put(".mac", "application/x-mac");
        put(".man", "application/x-troff-man");
        put(".math", "text/xml");
        put(".mfp", "application/x-shockwave-flash");
        put(".mht", "message/rfc822");
        put(".mhtml", "message/rfc822");
        put(".mi", "application/x-mi");
        put(".mid", "audio/mid");
        put(".midi", "audio/mid");
        put(".mil", "application/x-mil");
        put(".mml", "text/xml");
        put(".mnd", "audio/x-musicnet-download");
        put(".mns", "audio/x-musicnet-stream");
        put(".mocha", "application/x-javascript");
        put(".movie", "video/x-sgi-movie");
        put(".mp1", "audio/mp1");
        put(".mp2", "audio/mp2");
        put(".mp2v", "video/mpeg");
        put(".mp3", "audio/mp3");
        put(".mp4", "video/mpeg4");
        put(".mpa", "video/x-mpg");
        put(".mpd", "application/vnd.ms-project");
        put(".mpe", "video/x-mpeg");
        put(".mpeg", "video/mpg");
        put(".mpg", "video/mpg");
        put(".mpga", "audio/rn-mpeg");
        put(".mpp", "application/vnd.ms-project");
        put(".mps", "video/x-mpeg");
        put(".mpt", "application/vnd.ms-project");
        put(".mpv", "video/mpg");
        put(".mpv2", "video/mpeg");
        put(".mpw", "application/vnd.ms-project");
        put(".mpx", "application/vnd.ms-project");
        put(".mtx", "text/xml");
        put(".mxp", "application/x-mmxp");
        put(".net", "image/pnetvue");
        put(".nrf", "application/x-nrf");
        put(".nws", "message/rfc822");
        put(".odc", "text/x-ms-odc");
        put(".out", "application/x-out");
        put(".p10", "application/pkcs10");
        put(".p12", "application/x-pkcs12");
        put(".p7b", "application/x-pkcs7-certificates");
        put(".p7c", "application/pkcs7-mime");
        put(".p7m", "application/pkcs7-mime");
        put(".p7r", "application/x-pkcs7-certreqresp");
        put(".p7s", "application/pkcs7-signature");
        put(".pc5", "application/x-pc5");
        put(".pci", "application/x-pci");
        put(".pcl", "application/x-pcl");
        put(".pcx", "application/x-pcx");
        put(".pdx", "application/vnd.adobe.pdx");
        put(".pfx", "application/x-pkcs12");
        put(".pgl", "application/x-pgl");
        put(".pic", "application/x-pic");
        put(".pko", "application/vnd.ms-pki.pko");
        put(".pl", "application/x-perl");
        put(".plg", "text/html");
        put(".pls", "audio/scpls");
        put(".plt", "application/x-plt");
        put(".pot", "application/vnd.ms-powerpoint");
        put(".ppa", "application/vnd.ms-powerpoint");
        put(".ppm", "application/x-ppm");
        put(".pps", "application/vnd.ms-powerpoint");
        put(".pr", "application/x-pr");
        put(".prf", "application/pics-rules");
        put(".prn", "application/x-prn");
        put(".prt", "application/x-prt");
        put(".ptn", "application/x-ptn");
        put(".pwz", "application/vnd.ms-powerpoint");
        put(".r3t", "text/vnd.rn-realtext3d");
        put(".ra", "audio/vnd.rn-realaudio");
        put(".ram", "audio/x-pn-realaudio");
        put(".ras", "application/x-ras");
        put(".rat", "application/rat-file");
        put(".rdf", "text/xml");
        put(".rec", "application/vnd.rn-recording");
        put(".red", "application/x-red");
        put(".rgb", "application/x-rgb");
        put(".rjs", "application/vnd.rn-realsystem-rjs");
        put(".rjt", "application/vnd.rn-realsystem-rjt");
        put(".rlc", "application/x-rlc");
        put(".rle", "application/x-rle");
        put(".rm", "application/vnd.rn-realmedia");
        put(".rmf", "application/vnd.adobe.rmf");
        put(".rmi", "audio/mid");
        put(".rmj", "application/vnd.rn-realsystem-rmj");
        put(".rmm", "audio/x-pn-realaudio");
        put(".rmp", "application/vnd.rn-rn_music_package");
        put(".rms", "application/vnd.rn-realmedia-secure");
        put(".rmvb", "application/vnd.rn-realmedia-vbr");
        put(".rmx", "application/vnd.rn-realsystem-rmx");
        put(".rnx", "application/vnd.rn-realplayer");
        put(".rp", "image/vnd.rn-realpix");
        put(".rpm", "audio/x-pn-realaudio-plugin");
        put(".rsml", "application/vnd.rn-rsml");
        put(".rt", "text/vnd.rn-realtext");
        put(".rv", "video/vnd.rn-realvideo");
        put(".sam", "application/x-sam");
        put(".sat", "application/x-sat");
        put(".sdp", "application/sdp");
        put(".sdw", "application/x-sdw");
        put(".sit", "application/x-stuffit");
        put(".slb", "application/x-slb");
        put(".sld", "application/x-sld");
        put(".slk", "drawing/x-slk");
        put(".smi", "application/smil");
        put(".smil", "application/smil");
        put(".smk", "application/x-smk");
        put(".snd", "audio/basic");
        put(".sol", "text/plain");
        put(".sor", "text/plain");
        put(".spc", "application/x-pkcs7-certificates");
        put(".spl", "application/futuresplash");
        put(".spp", "text/xml");
        put(".ssm", "application/streamingmedia");
        put(".sst", "application/vnd.ms-pki.certstore");
        put(".stl", "application/vnd.ms-pki.stl");
        put(".stm", "text/html");
        put(".sty", "application/x-sty");
        put(".svg", "text/xml");
        put(".swf", "application/x-shockwave-flash");
        put(".tdf", "application/x-tdf");
        put(".tg4", "application/x-tg4");
        put(".tga", "application/x-tga");
        put(".tiff", "image/tiff");
        put(".tld", "text/xml");
        put(".top", "drawing/x-top");
        put(".torrent", "application/x-bittorrent");
        put(".tsd", "text/xml");
        put(".txt", "text/plain");
        put(".uin", "application/x-icq");
        put(".uls", "text/iuls");
        put(".vcf", "text/x-vcard");
        put(".vda", "application/x-vda");
        put(".vdx", "application/vnd.visio");
        put(".vml", "text/xml");
        put(".vpg", "application/x-vpeg005");
        put(".vss", "application/vnd.visio");
        put(".vsw", "application/vnd.visio");
        put(".vsx", "application/vnd.visio");
        put(".vtx", "application/vnd.visio");
        put(".vxml", "text/xml");
        put(".wav", "audio/wav");
        put(".wax", "audio/x-ms-wax");
        put(".wb1", "application/x-wb1");
        put(".wb2", "application/x-wb2");
        put(".wb3", "application/x-wb3");
        put(".wbmp", "image/vnd.wap.wbmp");
        put(".wiz", "application/msword");
        put(".wk3", "application/x-wk3");
        put(".wk4", "application/x-wk4");
        put(".wkq", "application/x-wkq");
        put(".wks", "application/x-wks");
        put(".wm", "video/x-ms-wm");
        put(".wma", "audio/x-ms-wma");
        put(".wmd", "application/x-ms-wmd");
        put(".wmf", "application/x-wmf");
        put(".wml", "text/vnd.wap.wml");
        put(".wmv", "video/x-ms-wmv");
        put(".wmx", "video/x-ms-wmx");
        put(".wmz", "application/x-ms-wmz");
        put(".wp6", "application/x-wp6");
        put(".wpd", "application/x-wpd");
        put(".wpg", "application/x-wpg");
        put(".wpl", "application/vnd.ms-wpl");
        put(".wq1", "application/x-wq1");
        put(".wr1", "application/x-wr1");
        put(".wri", "application/x-wri");
        put(".wrk", "application/x-wrk");
        put(".ws", "application/x-ws");
        put(".ws2", "application/x-ws");
        put(".wsc", "text/scriptlet");
        put(".wsdl", "text/xml");
        put(".wvx", "video/x-ms-wvx");
        put(".xdp", "application/vnd.adobe.xdp");
        put(".xdr", "text/xml");
        put(".xfd", "application/vnd.adobe.xfd");
        put(".xfdf", "application/vnd.adobe.xfdf");
        put(".xhtml", "text/html");
        put(".xlw", "application/x-xlw");
        put(".xml", "text/xml");
        put(".xpl", "audio/scpls");
        put(".xq", "text/xml");
        put(".xql", "text/xml");
        put(".xquery", "text/xml");
        put(".xsd", "text/xml");
        put(".xsl", "text/xml");
        put(".xslt", "text/xml");
        put(".xwd", "application/x-xwd");
        put(".x_b", "application/x-x_b");
        put(".sis", "application/vnd.symbian.install");
        put(".sisx", "application/vnd.symbian.install");
        put(".x_t", "application/x-x_t");
        put(".ipa", "application/vnd.iphone");
    }};
}
