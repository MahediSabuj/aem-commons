package com.aem.commons.core.configs.osgi;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
  name = "AEM Commons Application Configuration")
public @interface AppConfig {
  @AttributeDefinition(name = "App Name")
  String app_name();

  @AttributeDefinition(name = "API Endpoint")
  String api_endpoint();

  @AttributeDefinition(name = "App Client Id")
  String client_id();

  @AttributeDefinition(name = "App Client Secret")
  String client_secret();
}
