package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.EmailNotificationService;
import com.db.awmd.challenge.service.NotificationService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public Account getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	@Override
	public synchronized void transferFund(Account fromAccount, Account toAccount, BigDecimal amount) {
		if (fromAccount.getBalance().compareTo(amount) != -1) {
			fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
			toAccount.setBalance(toAccount.getBalance().add(amount));
			NotificationService notificationService = new EmailNotificationService();
			notificationService.notifyAboutTransfer(fromAccount,
					"Amount " + amount + " successfully tranferred to account " + toAccount.getAccountId());
			notificationService.notifyAboutTransfer(toAccount,
					"Amount " + amount + " creditted to your account");
		}
	}

}
