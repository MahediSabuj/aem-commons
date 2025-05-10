package com.aem.commons.core.services;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface ResourceResolverService {
    /**
     * Returns a ResourceResolver instance.
     *
     * @return ResourceResolver instance
     */
    ResourceResolver getResourceResolver();

}
