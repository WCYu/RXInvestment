package com.rxjy.rxinvestment.base;

/**
 * Created by 阿禹 on 2018/6/25.
 */

public class PathUrl {

    public static final String RS_API_HOST = "https://api.dcwzg.com:9191/";   //线上
    public static final String FID_API_HOST = "http://wpsnew.rxjy.com:9090/";//发现模块数据线上
    public static final String FID_HOST = "http://wpsnew.rxjy.com/";
    private static final String BAPI_HOSTMONEY = "http://idc.dcwzg.com:13207/";//线上钱包

    public static String TZXZJLJGURL = BAPI_HOSTMONEY + "investment/adminManager/result";//获取投资行政经理结果
    public static String TZXZJLGCURL = BAPI_HOSTMONEY + "investment/adminManager/process";//获取投资行政经理过程
    public static String TZXZJLFHURL = BAPI_HOSTMONEY + "investment/adminManager/dividend";//获取投资行政经理分红
    public static String TZXZJLJFURL = BAPI_HOSTMONEY + "investment/adminManager/rewardAndPunishment";//获取投资行政经理奖罚

    //线上钱包
    public static String MONEYURL = BAPI_HOSTMONEY + "investment/adminManager";//钱包信息行政经理
    public static String MONEYLISTURL = BAPI_HOSTMONEY + "actionapi/TZManage/RedPacketList";

    public static String ISSHOWREDDOTURL = FID_HOST + "a/sap/sapArticle/isNewArticle";  //是否有新文章
    public static String FBWENZHANGURL = FID_HOST + "a/sap/sapArticle/SaveArticle";

    //发现模块数据线上
    public static String FINDLISTURL = FID_API_HOST + "a/sap/sapArticle/getAppArticleList";//发现列表

    //线上

    public static String SQQJURL = RS_API_HOST + "ActionApi/TZManage/ApplicationLeave";//申请考勤/请假
    public static String QJTYPEURL = RS_API_HOST + "ActionApi/TZManage/GetLeaveStateList";//获取请假类型
    public static String HJTIJIAOURL = RS_API_HOST + "ActionApi/TZManage/AddHJToTZ";//环境提交
    public static String XXTIJIAOURL = RS_API_HOST + "ActionApi/TZManage/AddXXToTZ";//形象提交
    public static String SMLOGINURL = RS_API_HOST + "actionapi/AppLogin/EWMLogin";//扫码登陆
    public static String ISGENGXINURL = RS_API_HOST + "actionapi/AppCurrencyHome/IsAndroidUpdated";//判断是否有更新
    //    public static String NUMBERINFOURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetCheckUserInfo_RXPT";//账号信息
    public static String NUMBERINFOURL = RS_API_HOST + "/actionapi/AppLogin/GetCheckUserInfo";//新账号信息
    //    public static String LOGINURL = RS_API_HOST + "actionapi/AppCurrencyHome/AppLogin";//登陆
    public static String LOGINURL = RS_API_HOST + "/actionapi/AppLogin/Login ";//新登陆
    public static String GETCAODEURL = RS_API_HOST + "actionapi/AppHome/GetVcodeUpdatePwd";//获取验证码
    public static String RENZHENGCODEURL = RS_API_HOST + "actionapi/AppLogin/GetInsideVcodeLanding";//新获取激活验证码
    public static String GETUSERINFOURL = RS_API_HOST + "/actionapi/AppCurrencyHome/GetAppCheckToken";//获取用户信息
    public static String MEMBERINFOURL = RS_API_HOST + "/actionapi/AN_Home/ShowMyInfo";//获取会员用户信息

    public static String BANNERLISTGURL = RS_API_HOST + "actionapi/AppPort/GetAppHomeBanner";//首页banner
    public static String NEWYUANGONGURL = RS_API_HOST + "actionapi/GroupManage/GetAppState";//新入职员工信息
    public static String ICONIMGURL = RS_API_HOST + "actionapi/AppCurrencyHome/UploadAvatar";//上传头像cardNo
    public static String USERINFOIMGURL = RS_API_HOST + "actionapi/AppCurrencyHome/entryUpdateImages";//上传个人资料图片
    public static String SETUSERINFOURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetUpdateUserData";//修改个人资料/入职资料
    public static String USERINFOURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetUserData";//获取个人资料
    public static String BANKLISTURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetBanklist";//银行列表/actionapi/AppCurrencyHome/GetBanklist
    public static String GETIMGSURL = RS_API_HOST + "actionapi/AppPort/GetImgList";//获取图片详情
    public static final String ZHUANZHNEGURL = RS_API_HOST + "actionapi/KGManage/ZhuanzhengBanli";  //申请转正
    public static final String ZHUANZHNEDATAGURL = RS_API_HOST + "actionapi/KGManage/GetAppZhuanzheng";//获取转正状态
    public static final String WEBURL_ENTRYJOBURL = RS_API_HOST + "home/ElectronicProtocol?";//入职需知，瑞祥之歌，瑞祥准则url
    public static String BANBENNOURL = RS_API_HOST + "actionapi/AppCurrencyHome/IsAndroidUpdated";  //版本号
    public static String HUANYINGDATAURL = RS_API_HOST + "actionapi/KGManage/GetWelcomes";  //获取欢迎信息
    public static String ZSDATIURL = RS_API_HOST + "actionapi/KGManage/AddSalesFirstTrial";//招商答题提价接口
    public static String ISAGREEURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetaIsAgreeElectronicProtocol";//是否同意协议
    public static String AGREEURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetaAgreeElectronicProtocol";//同意协议
    public static String SETPSWURL = RS_API_HOST + "actionapi/AppHome/UpdatePassword_Vcode";//修改密码
    public static String TPKENURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetAppCheckToken";// 检查Token
    public static String USERDATAURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetUserData";// 用户信息
    public static String GETXINGXIANGURL = RS_API_HOST + "actionApi/TZManage/GetAreasListToTZ";//获取形象/环境主页
    public static String CHUFALISTRL = RS_API_HOST + "actionApi/TZManage/GetUserListToTZ";//投资获取处罚人员列表
    public static String GETXINGXIANIMGGURL = RS_API_HOST + "actionApi/TZManage/GetAreasStandardToTZ";//获取环境标准图片
    public static String USERTYPEURL = RS_API_HOST + "actionApi/TZManage/GetIsManageUser";//获取是否是管理层
    public static String GOMEDATAURL = RS_API_HOST + "actionapi/KGManage/GetNewsList";//获取首页信息
    public static String HOMEBANNERURL = RS_API_HOST + "actionapi/AppPort/GetAppHomeBanner";//获取首页Banner
    public static String ZHAOSHANGUSERURL = RS_API_HOST + "actionapi/AN_Home/ShowMyInfo";//获取招商用户信息
    public static String SETZHAOSHANGUSERURL = RS_API_HOST + "actionapi/AN_Home/UpdateMyInfo";//设置招商用户信息
    public static String IMGURL = RS_API_HOST + "actionapi/AN_Home/UpdateImages";//图片上传
    public static String QINGJIALISTURL = RS_API_HOST + "ActionApi/TZManage/GetLeaveList";//请假列表

    public static String MESSAGENUMURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetAppNoticeNoReadCount";//获取消息数量
    public static String WBMESSAGENUMURL = RS_API_HOST + "actionapi/JiGuang/GetAppNoticeNoReadCount";//获取外部消息数量
    public static String MESSAGELISTURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetAppNoticeGroup";//消息列表
    public static String WBMESSAGELISTURL = RS_API_HOST + "actionapi/JiGuang/GetAppNoticeGroup";//外部消息列表
    public static String REDTASKURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetAppNoticeList";//红包任务列表
    public static String WBREDTASKURL = RS_API_HOST + "actionapi/JiGuang/GetAppNoticeList";//外部红包任务列表
    public static String TONGZHIINFOURL = RS_API_HOST + "actionapi/AppCurrencyHome/GetAppNoticeDetail";//获取通知详情
    public static String WBTONGZHIINFOURL = RS_API_HOST + "/actionapi/JiGuang/GetAppNoticeDetail";//获取外部通知详情
}
