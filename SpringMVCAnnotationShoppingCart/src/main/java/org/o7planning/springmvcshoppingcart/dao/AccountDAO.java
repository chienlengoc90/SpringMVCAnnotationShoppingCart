package org.o7planning.springmvcshoppingcart.dao;

import org.o7planning.springmvcshoppingcart.entity.Account;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface AccountDAO {

	public Account findAccount(String userName);

}