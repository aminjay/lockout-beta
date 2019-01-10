package com.LockOut.Server.Repositories;

import javax.transaction.Transactional;

import com.LockOut.Server.Models.Message;

@Transactional
public interface MessageRepository extends BaseMessageRepository<Message, Long> {
	
}



