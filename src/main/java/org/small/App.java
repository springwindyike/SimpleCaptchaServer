package org.small;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import fi.iki.elonen.NanoHTTPD;
import org.small.model.Result;
import org.small.utils.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 程序入口
 */
public class App extends NanoHTTPD {
    public App() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe.getMessage());
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
//        http://localhost:8080/?username=11
        Map<String, String> httpParms = session.getParms();
        CaptchaService captchaService = new CaptchaService();
        int width = StringUtils.isEmpty(httpParms.get("width").toString()) ? 0 : Integer.parseInt(httpParms.get("width").toString());
        int height = StringUtils.isEmpty(httpParms.get("height").toString()) ? 0 : Integer.parseInt(httpParms.get("height").toString());
        int len = StringUtils.isEmpty(httpParms.get("len").toString()) ? 0 : Integer.parseInt(httpParms.get("len").toString());
        Result result = captchaService.createSimpleCode(width, height, len,
                httpParms.get("style").toString(),
                httpParms.get("output").toString());
        String returnMsg = JSONObject.toJSONString(result, SerializerFeature.BrowserCompatible);
        try {
            returnMsg = URLDecoder.decode(returnMsg,"utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return newFixedLengthResponse(returnMsg);
    }
}