package com.example.librarymanagement.extension;

import android.content.Context;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class StartConversation {

    public void startConversation(String reciverId, Context context){
        Conversation.ConversationType conversationType  = Conversation.ConversationType.PRIVATE;
        String targetId = reciverId;
        String title = "这里可以填写名称";

        RongIM.getInstance().startConversation(context , conversationType, targetId, title, null);

    }
}
