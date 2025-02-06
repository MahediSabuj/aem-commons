package com.aem.commons.core.services.impl;

import com.aem.commons.core.configs.osgi.AppConfig;
import com.aem.commons.core.services.AppConfigService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = AppConfigService.class)
@Designate(ocd = AppConfig.class)
public class AppConfigServiceImpl implements AppConfigService {
  private AppConfig appConfig;

  @Activate
  public void activate(AppConfig appConfig) {
    this.appConfig = appConfig;
  }
  
  @Override
  public String getAppName() {
    return appConfig.app_name();
  }

  @Override
  public String getAPIEndpoint() {
    return appConfig.api_endpoint();
  }

  @Override
  public String getClientId() {
    return appConfig.client_id();
  }

  @Override
  public String getClientSecret() {
    return appConfig.client_secret();
  }
}