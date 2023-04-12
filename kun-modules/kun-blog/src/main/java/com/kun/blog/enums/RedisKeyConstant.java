package com.kun.blog.enums;

/**
 * redis 键常量类
 *
 * @author hezhidong
 * @date 2022/4/25 下午4:31
 */
public class RedisKeyConstant {
    public final static String INVITE_CODE_LOCK_PREFIX = "inviteCode-lock:";

    public final static String PERSON_CENTER_PREFIX = "PERSON_CENTER:";
    public final static String PERSON_CENTER_LOCK = "LOCK:";

    /**
     * 社交模块相关
     */
    public final static String SOCIAL_LOCK = "SOCIAL:";
    public final static String USER_FOLLOW_LOCK = PERSON_CENTER_PREFIX + PERSON_CENTER_LOCK + SOCIAL_LOCK + "FOLLOW:";
    /**
     * 缓存用户社交基本信息键值（获赞量，关注数，粉丝数等）
     */
    public final static String SOCIAL_USER_BASIC_INFO = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "USER_BASIC_INFO:";
    /**
     * 用户端经纬度键值
     */
    public final static String SOCIAL_USER_GEO = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "GEO:";
    /**
     * 缓存优质动态的主键ID键值
     */
    public final static String SOCIAL_SUPERIOR_DYNAMIC = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "SUPERIOR_DYNAMIC";
    /**
     * 缓存一周内普通动态的主键ID键值
     */
    public final static String SOCIAL_GENERAL_DYNAMICS = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "SOCIAL_GENERAL_DYNAMICS";
    /**
     * 缓存一周外普通动态的主键ID键值
     */
    public final static String SOCIAL_OLD_DYNAMIC = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "SOCIAL_OLD_DYNAMIC";
    /**
     * 缓存兴趣活动
     */
    public final static String SOCIAL_ACTIVITY = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "SOCIAL_ACTIVITY";
    /**
     * 缓存约聊订单
     */
    public final static String SOCIAL_CHAT = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "SOCIAL_CHAT";


    /**
     * 当前用户访问过的动态键值
     */
    public final static String SOCIAL_CURRENT_USER_VISITED = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "CURRENT_USER_VISITED:";


    /**
     * 挑战赛报名键值
     */
    public final static String CHALLENGE_ENTRY = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "CHALLENGE_ENTRY:";
    /**
     * 挑战赛补差价升级键值
     */
    public final static String CHALLENGE_UPGRADE = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "CHALLENGE_UPGRADE:";
    /**
     * 挑战赛支付回调键值
     */
    public final static String CHALLENGE_PAY_CALLBACK = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "CHALLENGE_PAY_CALLBACK:";
    /**
     * 挑战赛瓜分收益键值
     */
    public final static String CHALLENGE_MONEY_GRANT = PERSON_CENTER_PREFIX + SOCIAL_LOCK + "CHALLENGE_MONEY_GRANT:";
}
