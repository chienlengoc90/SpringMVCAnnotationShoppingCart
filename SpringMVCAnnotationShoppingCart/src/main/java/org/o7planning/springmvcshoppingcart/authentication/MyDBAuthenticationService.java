package org.o7planning.springmvcshoppingcart.authentication;

import java.util.ArrayList;
import java.util.List;

import org.o7planning.springmvcshoppingcart.converter.AccountConverter;
import org.o7planning.springmvcshoppingcart.dao.AccountDAO;
import org.o7planning.springmvcshoppingcart.entity.Account;
import org.o7planning.springmvcshoppingcart.model.AccountDTO;
import org.o7planning.springmvcshoppingcart.model.MyUserDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyDBAuthenticationService implements UserDetailsService {

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private AccountConverter accountConverter;

	@Override
	public MyUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = accountDAO.findAccount(username);
		System.out.println("Account= " + account);

		AccountDTO accountDTO = accountConverter.convertToDto(account);

		if (accountDTO == null) {
			throw new UsernameNotFoundException("User " //
					+ username + " was not found in the database");
		}

		// EMPLOYEE,MANAGER,..
		String role = accountDTO.getUserRole();

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		// ROLE_EMPLOYEE, ROLE_MANAGER
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

		authorities.add(authority);

		boolean enabled = account.isActive();
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		MyUserDetail myUserDetail = new MyUserDetail(accountDTO.getUserName(), accountDTO.getPassword(), enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

		BeanUtils.copyProperties(accountDTO, myUserDetail);
		return myUserDetail;
	}

}