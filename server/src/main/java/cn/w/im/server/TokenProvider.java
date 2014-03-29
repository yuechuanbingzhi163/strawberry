package cn.w.im.server;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-24 下午2:53.
 * Summary: provide create token arithmetic.
 */
public interface TokenProvider {

    /**
     * create token str.
     * @return token String.
     */
    String create();
}