package com.aem.commons.core.services;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.annotation.versioning.ConsumerType;

import java.util.List;

@ConsumerType
public interface AssetService {
    /**
     * Deletes multiple assets at the given paths.
     *
     * @param assetPaths the paths of the assets to delete
     * @return true if all assets were deleted successfully, false otherwise
     */
    boolean deleteAssets(List<String> assetPaths);
}
