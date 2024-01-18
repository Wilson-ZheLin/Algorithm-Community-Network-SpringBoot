package com.zhelin.community.dao;

import com.zhelin.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    // All conversations
    List<Message> selectConversations(int userId, int offset, int limit);

    // Conversations count
    int selectConversationsCount(int userId);

    // Messages
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // Messages count
    int selectLettersCount(String conversationId);

    // Unread messages count
    int selectLetterUnreadCount(int userId, String conversationId);

    int insertMessage(Message message);

    int updateStatus(List<Integer> ids, int status);
}
