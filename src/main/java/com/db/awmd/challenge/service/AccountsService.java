package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  
  public void transferFund(String accountFromId, String accountToId, BigDecimal amountToTransfer) throws AccountNotFoundException{
	  Account fromAccount= getAccount(accountFromId);
	  if(fromAccount == null){
		  throw new AccountNotFoundException("Account does not exist");
	  }
	  Account toAccount= getAccount(accountToId);
	  if(toAccount == null){
		  throw new AccountNotFoundException("Account does not exist");
	  }
	  this.accountsRepository.transferFund(fromAccount, toAccount, amountToTransfer);
  }
}
