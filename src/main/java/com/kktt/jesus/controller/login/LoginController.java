//package com.kktt.jesus.controller.login;
//
//import com.google.gson.Gson;
//import com.kktt.jesus.utils.HttpClientUtil;
//import com.kktt.jesus.common.Constant;
//import com.kktt.jesus.controller.BaseController;
//import com.kktt.jesus.controller.viewobject.WXSession;
//import com.kktt.jesus.response.CommonReturnType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/wx_login")
//public class LoginController extends BaseController {
//
//
//    @RequestMapping("/login")
//    @ResponseBody
//    public CommonReturnType login(String code){
//
////        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session?" +
////                "appid=APPID" +
////                "&secret=SECRET" +
////                "&js_code=JSCODE" +
////                "&grant_type=authorization_code";
//        String url = "https://api.weixin.qq.com/sns/jscode2session";
//        Map<String,String> params = new HashMap<>();
//        params.put("appid", Constant.WX_APP_INFO.WX_APP_ID);
//        params.put("secret",Constant.WX_APP_INFO.WX_SECRET);
//        params.put("js_code",code);
//        params.put("grant_type","authorization_code");
//        String jsonStr = HttpClientUtil.doGet(url,params);
//        Gson gson = new Gson();
//        WXSession wxSession = gson.fromJson(jsonStr,WXSession.class);
//        return CommonReturnType.create(wxSession);
//    }
//
//}
