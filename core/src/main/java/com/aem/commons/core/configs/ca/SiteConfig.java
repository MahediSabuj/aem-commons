package com.aem.commons.core.configs.ca;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(name = "siteConfig", label = " AEM Commons Site Configuration")
public @interface SiteConfig {
    @Property(label = "Content Path")
    String contentPath();

    @Property(label = "Site Domain")
    String siteDomain();

    @Property(label = "Content Approver Group")
    String approverGroup();
}
