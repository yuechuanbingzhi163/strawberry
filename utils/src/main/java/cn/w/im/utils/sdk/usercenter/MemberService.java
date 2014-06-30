package cn.w.im.utils.sdk.usercenter;

import cn.w.im.utils.sdk.usercenter.config.UcConfig;
import cn.w.im.utils.sdk.usercenter.model.Account;
import cn.w.im.utils.sdk.usercenter.model.MemberProfile;
import cn.w.im.utils.sdk.usercenter.model.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Creator: JimmyLin
 * DateTime: 14-6-30 上午9:59
 * Summary:
 */
public class MemberService extends UserCenterSupport implements Members{

    /**
     * 通过w号获取会员资料
     *
     * @param wid
     * @return 会员资料
     */
    @Override
    public MemberProfile getByWid(String wid) throws UcException {
        String url = UcConfig.getValue("baseURL")+"member/"+wid;
        return get(url, MemberProfile.class);
    }

    /**
     * 登录校验
     *
     * @param account
     * @return 校验成功与否
     */
    @Override
    public boolean verify(Account account) throws UcException {
        String url =  UcConfig.getValue("baseURL")+"member/"+account.getWid()+"/verify";
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", MD5Util.twiceMd5(account.getPassword()));
        Response response = post(url, params, Response.class);
        return response.isSuccess();
    }


    public static void main(String[] args) throws UcException {
        Members members = new MemberService();
        MemberProfile memberProfile = members.getByWid("1002885");
        System.out.println(memberProfile.toString());
        System.out.println(members.verify(new Account("1000273", "w123456")));
    }

}