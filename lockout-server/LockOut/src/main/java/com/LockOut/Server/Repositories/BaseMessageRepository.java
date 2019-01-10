package com.LockOut.Server.Repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.LockOut.Server.Models.BaseMessage;


@NoRepositoryBean
public interface BaseMessageRepository<T extends BaseMessage, id extends Serializable> extends CrudRepository<T, id> {
	@Query("select e from #{#entityName} e")
	List<T> findBySender(String Sender);
	List<T> findByRecipient(String Recipient);
	List<T> findByRecipientOrSender(String Recipient, String Sender);
}
