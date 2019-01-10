package com.LockOut.Server.Repositories;



import javax.transaction.Transactional;

import com.LockOut.Server.Models.GroupMessage;

@Transactional
public interface GroupMessageRepository extends BaseMessageRepository<GroupMessage, Long>
{
	
}