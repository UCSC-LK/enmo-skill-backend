package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatModel {
    String name,lastmsg,chatid;

    public ChatModel(String name, String lastmsg, String chatid) {
        this.name = name;
        this.lastmsg = lastmsg;
        this.chatid = chatid;
    }

    public  ChatModel(ResultSet resultSet) throws SQLException {
         this.name = resultSet.getString("other_user_username");
         this.lastmsg = resultSet.getString("last_message_content");
        this.chatid = resultSet.getString("chat_id");

     }
}
