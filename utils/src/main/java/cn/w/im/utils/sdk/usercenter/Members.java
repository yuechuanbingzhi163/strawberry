package cn.w.im.utils.sdk.usercenter;

import cn.w.im.utils.sdk.usercenter.model.Account;
import cn.w.im.utils.sdk.usercenter.model.MemberProfile;

/**
 * Creator: JimmyLin
 * DateTime: 14-6-30 下午1:40
 * Summary:
 */
public interface Members {

    MemberProfile getByWid(String wid) throws UcException;

    boolean verify(Account account) throws UcException;
}