/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package izhipeng.dailybuy;

import java.io.File;

public class Config {
    //  temp 当前的收藏数量
    public static void setUserCollectionCount(int userCollectionCount) {
        USER_COLLECTION_COUNT = userCollectionCount;
    }

    public static int USER_COLLECTION_COUNT = 0;


    //   告诉ChildFfag_s 要不要restartLoader
    public static boolean FriendsNeedRefresh = false;
    public static boolean AttetionNeedRefresh = false;
    public static boolean FansNeedRefresh = false;
    public static boolean InVitedNeedRefresh = false;
    public static boolean PrivateChatNeedRefresh = false;

    // 定义follow type


    //	public static final String TYPE_NEW_FRIENDS_UID = "item_new_friends";
//	public static final String GROUP_USERNAME = "item_groups";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String ACCOUNT_REMOVED = "account_removed";


    public static final int REQUEST_PHONE_CONTACT = 401;
    public static final String FOLLOW_TYPE_ALL = "0";
    public static final int FOLLOW_TYPE_FANS = 1; //粉丝
    public static final int FOLLOW_TYPE_ATTENTION = 2;//关注
    public static final int FOLLOW_TYPE_FRIENDS = 3;//好友
    public static final int FOLLOW_TYPE_INVITED = 4;//邀请你的人
    public static final int FOLLOW_TYPE_STRANGER = -1;//todo 陌生人

    // 假定的  类似于第三方的联系人，虽然是陌生人，但又不是完全的陌生人，或者说陌生人也有分类的
    public static final String FOLLOW_TYPE_GROUP_USERNAME = "item_groups";

//  准备加入的分组

//   假定的  类似于陌陌的推荐板块，或说HXdemo的 申请与通知版块
//    public static final int USER_TYPE_ID_NEW_FRIENDS = -1;//不满足互斥条件，作为userid 这也可以吗？？
//    public static final int USER_TYPE_ID_NEW_FUNS = -2;//仍然是userid

    public static final int USER_TYPE_ID__CUSTOM_GROUP = -3;


    public static final String TYPE_NEW_FRIENDS_UID = "new_friends_uid";
    public static final String TYPE_NEW_FUNS_UID = "new_funs_uid";

    public static int UNREAD_FANS_NUM = 0;
    public static int UNREAD_FRIENDS_NUM = 0;
    public static int UNREAD_INVEITED_NUM = 0;
    // 被评论
    public static int UNREAD_ACK_COMMENT = 0;
    public static int UNREAD_NEW_PORTFOLIO = 0;
    //   未读消息标志
    public static int UN_READ_JPUSH_MSG = 0; //通知


//    public static int UN_READ_CHAT_MSG = 0; //私聊
    //   这是总的未读消息数，activity 's bottom icon uses

    //  异常退出 logout

    //    个人资料页面是否刷新的flag
    public static boolean isUserNeedRefresh = false;
}
