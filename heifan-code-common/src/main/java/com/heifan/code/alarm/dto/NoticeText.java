package com.heifan.code.alarm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HiF
 * @date 2022/3/8 10:21
 */
public class NoticeText {


    /**
     * 飞书通知参数类
     */
    @Getter
    @Setter
    public static class FeiShuText{

        /**
         * 通知类型：可传入：text（文本）/ post（富文本）/ image（图片）/ share_chat（分享群名片）/ interactive（消息卡片）
         */
        String msg_type = "text";

        FeiShuTextContent content;

        /**
         * 需要额外通知的人
         */
        String[] atMobiles;

        @Getter
        @Setter
        public static class FeiShuTextContent{
            String text;
            public FeiShuTextContent(String text){
                this.text = text;
            }
        }

        public FeiShuText(String text){
            this.content = new FeiShuTextContent(text);
        }

        public FeiShuText(String msgStr, String[] atMobiles) {
            this.content = new FeiShuTextContent(msgStr);
            this.atMobiles = atMobiles;
        }

    }

    /**
     * 企业微信通知参数类
     */
    @Getter
    @Setter
    public static class WeChatText {

        /**
         * 通知类型
         */
        String msgtype = "markdown";

        /**
         * 通知内容
         */
        WeChatMarkDownText markdown;

        /**
         * 需要额外通知的人
         */
        String[] mentioned_mobile_list;

        /**
         * 企业微信通知内容类
         */
        @Getter
        @Setter
        public static class WeChatMarkDownText {

            /**
             * 通知内容
             */
            String content;

            public WeChatMarkDownText(String markdown) {
                this.content = markdown;
            }
        }

        public WeChatText(String markdown, String[] mentioned_mobile_list) {
            this.markdown = new WeChatMarkDownText(markdown);
            this.mentioned_mobile_list = mentioned_mobile_list;
        }
    }

    /**
     * 钉钉通知参数类
     */
    @Getter
    @Setter
    public static class DingTalkText {

        /**
         * 通知类型
         */
        String msgtype = "markdown";

        /**
         * 通知内容
         */
        DingTalkMarkDownText markdown;

        /**
         * 需要额外通知的人
         */
        String[] atMobiles;

        /**
         * 钉钉通知内容类
         */
        @Getter
        @Setter
        public static class DingTalkMarkDownText {

            /**
             * 简略标题（会话列表显示）
             */
            String title;

            /**
             * 会话内实际显示的内容
             */
            String text;

            public DingTalkMarkDownText(String markdown) {
                this.title = "告警信息";
                this.text = markdown;
            }

        }

        public DingTalkText(String markdown, String[] atMobiles) {
            this.markdown = new DingTalkMarkDownText(markdown);
            this.atMobiles = atMobiles;
        }
    }

}
