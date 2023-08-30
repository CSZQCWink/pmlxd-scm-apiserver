package com.sungeon.bos.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 编程式事务执行器
 *
 * @author 刘国帅
 * @date 2023-1-3
 **/
@Slf4j
@Component
public class TransactionHandler {

	@Autowired
	private PlatformTransactionManager transactionManager;

	/**
	 * @param propagationBehavior 传播行为：PROPAGATION_REQUIRED、PROPAGATION_SUPPORTS、PROPAGATION_MANDATORY、
	 *                            PROPAGATION_REQUIRES_NEW、PROPAGATION_NOT_SUPPORTED、PROPAGATION_NEVER、
	 *                            PROPAGATION_NESTED
	 * @param statement           执行代码
	 * @return boolean
	 */
	public boolean transactional(int propagationBehavior, Runnable statement) {
		return transactional(propagationBehavior, TransactionDefinition.ISOLATION_DEFAULT,
				TransactionDefinition.TIMEOUT_DEFAULT, statement);
	}

	/**
	 * @param propagationBehavior 传播行为：PROPAGATION_REQUIRED、PROPAGATION_SUPPORTS、PROPAGATION_MANDATORY、
	 *                            PROPAGATION_REQUIRES_NEW、PROPAGATION_NOT_SUPPORTED、PROPAGATION_NEVER、
	 *                            PROPAGATION_NESTED
	 * @param isolationLevel      隔离级别
	 * @param statement           执行代码
	 * @return boolean
	 */
	public boolean transactional(int propagationBehavior, int isolationLevel, Runnable statement) {
		return transactional(propagationBehavior, isolationLevel, TransactionDefinition.TIMEOUT_DEFAULT, statement);
	}

	/**
	 * @param propagationBehavior 传播行为：PROPAGATION_REQUIRED、PROPAGATION_SUPPORTS、PROPAGATION_MANDATORY、
	 *                            PROPAGATION_REQUIRES_NEW、PROPAGATION_NOT_SUPPORTED、PROPAGATION_NEVER、
	 *                            PROPAGATION_NESTED
	 * @param isolationLevel      隔离级别
	 * @param timeout             超时时间，单位：秒
	 * @param statement           执行代码
	 * @return boolean
	 */
	public boolean transactional(int propagationBehavior, int isolationLevel, int timeout, Runnable statement) {
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		transactionDefinition.setPropagationBehavior(propagationBehavior);
		transactionDefinition.setIsolationLevel(isolationLevel);
		transactionDefinition.setTimeout(timeout);
		return transactional(transactionDefinition, statement);
	}

	/**
	 * @param transactionDefinition 事务定义
	 * @param statement             执行代码
	 * @return boolean
	 */
	public boolean transactional(TransactionDefinition transactionDefinition, Runnable statement) {
		TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
		try {
			// 运行方法
			statement.run();

			transactionManager.commit(status);
			return true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public boolean transactional(Runnable statement) {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			// 运行方法
			statement.run();

			transactionManager.commit(status);
			return true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			log.error(e.getMessage(), e);
			return false;
		}
	}

}
