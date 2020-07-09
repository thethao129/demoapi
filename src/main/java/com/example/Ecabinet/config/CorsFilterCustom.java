//package com.example.Ecabinet.config;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Configuration
//public class CorsFilterCustom extends OncePerRequestFilter{
//
////	public CORSFilter(CorsConfigurationSource configSource) {
////		super(configSource);
////		// TODO Auto-generated constructor stub
////	}
//	
//	@Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.addHeader("Access-Control-Expose-Headers", "Authorization,Link,X-Total-Count");
//        response.addHeader("Access-Control-Allow-Credentials", "true");
//        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            filterChain.doFilter(request, response);
//        }
//    }
//
//}
