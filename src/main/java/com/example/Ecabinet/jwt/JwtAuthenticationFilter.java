package com.example.Ecabinet.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Ecabinet.dao.impl.AccountDaoImpl;
import com.example.Ecabinet.entity.CurrentUser;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JWTutil jwtUtil;

	@Autowired
	private AccountDaoImpl accountDaoImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String jwt = getJwtFromRequest(request);

		try {
			if (jwt != null && jwtUtil.validateToken(jwt)) {
				String username = jwtUtil.getUserNameFromJwtToken(jwt);
				CurrentUser currentUser = (CurrentUser) accountDaoImpl.loadUserByUsername(username);
				if (currentUser != null) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							currentUser, null, currentUser.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception ex) {
			System.out.println("failed on set user authentication");
		}

		filterChain.doFilter(request, response);

	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
