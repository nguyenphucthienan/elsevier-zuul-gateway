package com.elsevier.elsevierzuulgateway.security;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ZuulPreFilter extends ZuulFilter {

  private static final Logger logger = LoggerFactory.getLogger(ZuulPreFilter.class);

  private final JwtConfig jwtConfig;

  public ZuulPreFilter(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext context = RequestContext.getCurrentContext();
    HttpServletRequest request = context.getRequest();

    // Add a custom header in the request
    context.addZuulRequestHeader(jwtConfig.getHeader(), request.getHeader(jwtConfig.getHeader()));
    logger.info(
        String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

    return null;
  }
}
