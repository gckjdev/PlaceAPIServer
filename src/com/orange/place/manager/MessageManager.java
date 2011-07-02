package com.orange.place.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import com.orange.common.cassandra.CassandraClient;
import com.orange.common.utils.DateUtil;
import com.orange.place.constant.DBConstants;
import com.orange.place.dao.IdGenerator;
import com.orange.place.dao.Message;

public class MessageManager extends CommonManager {

	public static Message createMessage(CassandraClient cassandraClient,
			String appId, String fromUserId, String toUserId,
			String messageContent) {

		String messageId = IdGenerator.generateId();

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(DBConstants.F_MESSAGEID, messageId);
		map.put(DBConstants.F_FROM_USERID, fromUserId);
		map.put(DBConstants.F_TO_USERID, toUserId);
		map.put(DBConstants.F_MESSAGE_CONTENT, messageContent);
		map.put(DBConstants.F_CREATE_DATE, DateUtil.currentDate());
		map.put(DBConstants.F_CREATE_SOURCE_ID, appId);
		log.info("<createMessage> messageId=" + messageId + ", fromUserId="
				+ fromUserId);
		cassandraClient.insert(DBConstants.MESSAGE, messageId, map);
		return new Message(map);
	}

	public static void createUserPostIndex(CassandraClient cassandraClient,
			String fromUserId, String toUserId, String messageId) {
		UUID uuid = UUID.fromString(messageId);
		cassandraClient.insert(DBConstants.INDEX_MY_MESSAGE, fromUserId, uuid,
				toUserId);
		cassandraClient.insert(DBConstants.INDEX_MY_MESSAGE, toUserId, uuid,
				fromUserId);
	}

	public static void deleteMessage(CassandraClient cassandraClient,
			String userId, String messageId) {
		// TODO Auto-generated method stub
		UUID uuid = UUID.fromString(messageId);
		cassandraClient.deleteUUIDColumn(DBConstants.INDEX_MY_MESSAGE, userId,
				uuid);
	}

	public static List<Message> getMyMessage(CassandraClient cassandraClient,
			String userId, String afterTimeStamp, String maxCount) {
		// TODO Auto-generated method stub
		UUID endUUID = getTimeStampUUID(afterTimeStamp);
		List<HColumn<UUID, String>> resultList = null;
		if (afterTimeStamp != null) {
			int max = getMaxCount(maxCount);
			resultList = cassandraClient.getColumnKeyByRange(
					DBConstants.INDEX_MY_MESSAGE, userId, null, endUUID, max);
		} else {
			int max = DEFAULE_MESSAGE_COUNT;
			resultList = cassandraClient.getColumnKeyByRange(
					DBConstants.INDEX_MY_MESSAGE, userId, null, max);
		}
		if (resultList == null) {
			return null;
		}
		List<Message> messageList = getMessageList(cassandraClient, resultList,
				userId);
		return messageList;

	}

	private static List<Message> getMessageList(
			CassandraClient cassandraClient,
			List<HColumn<UUID, String>> messageIdIndexList, String userId) {
		int size = messageIdIndexList.size();

		String[] messageIds = new String[size];
		int i = 0;
		for (HColumn<UUID, String> result : messageIdIndexList) {
			String messageId = result.getName().toString();
			messageIds[i++] = messageId;
		}

		Rows<String, String, String> rows = cassandraClient.getMultiRow(
				DBConstants.MESSAGE, messageIds);
		if (rows == null) {
			return null;
		}
		// convert rows to List<Message>
		// change the implementation to sort the return result in right order
		List<Message> messageList = new ArrayList<Message>();
		int count = messageIds.length;
		String userIds[] = new String[count];
		for (i = 0; i < count; i++) {
			Row<String, String, String> row = rows.getByKey(messageIds[i]);
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			if (columnSlice != null) {
				List<HColumn<String, String>> columns = columnSlice
						.getColumns();
				if (columns != null) {
					Message message = new Message(columns);
					String messageType = null;
					// get the message type
					if (!userId.equals(message.getFromUserId())) {
						messageType = MESSAGE_TYPE_RECEIVE;
						userIds[i] = message.getFromUserId();
					} else {
						messageType = MESSAGE_TYPE_SEND;
						userIds[i] = message.getToUserId();
					}
					message.addValues(DBConstants.F_MESSAGE_TYPE, messageType);
					messageList.add(message);
				}
			}
		}

		Rows<String, String, String> userRows = cassandraClient.getMultiRow(
				DBConstants.USER, userIds, DBConstants.F_USERID,
				DBConstants.F_NICKNAME, DBConstants.F_AVATAR, DBConstants.F_GENDER);

		for (Message message : messageList) {
			// set user nickname and avatar to message
			String uid = null;
			String messageType = message.getMessageType();
			if (messageType.equals(MESSAGE_TYPE_SEND)) {
				uid = message.getToUserId();
			} else {
				uid = message.getFromUserId();
			}
			if (uid != null) {
				Row<String, String, String> userRow = userRows.getByKey(uid);
				ColumnSlice<String, String> columnSlice = userRow
						.getColumnSlice();
				if (columnSlice != null) {
					List<HColumn<String, String>> columns = columnSlice
							.getColumns();
					if (columns != null) {
						message.addValues(columns);
					}
				}
			}
		}

		return messageList;
	}
}
