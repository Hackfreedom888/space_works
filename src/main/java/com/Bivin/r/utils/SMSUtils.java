package com.Bivin.r.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;



public class SMSUtils {

    //②定义了一个方法sendShortMessage:需要传入电话号码（自己固定也可）和验证码（通过工具类生成，自己定义都可以）
    public static void sendShortMessage(String telephone,String code) throws ClientException {

        // ③初始化ascClient需要的几个参数（固定写法，粘贴复制即可）
        final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）

        //④输入The region ID、The AccessKey ID of the RAM account、The AccessKey Secret of the RAM account（在自己的阿里云中申请AccessKey，修改值）
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "xxxx", "xxxx");

        //⑤引入下列代码，固定
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/
        IAcsClient client = new DefaultAcsClient(profile);


        //⑥测试中setSignName只能是阿里云短信测试不可修改，即唯一测试专用签名
        //setTemplateCode是自己对应的模板Code
        SendSmsRequest request = new SendSmsRequest();
        request.setSignName("阿里云短信测试");
        //⑦修改为自己的模板Code
        request.setTemplateCode("SMS_153950927");
        //⑧使用传入的telephone，作为要接收发送的手机号
        request.setPhoneNumbers(telephone);
        //⑨使用自己随机生成的验证码
        request.setTemplateParam("{\"code\":\""+code+"\"}");


        try {
            //调用是否成功，是否会出现错误，状态码---错误信息
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            //打印出出错信息
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}
