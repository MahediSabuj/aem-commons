package com.aem.commons.core.services.impl;

import com.aem.commons.core.services.AssetService;
import com.aem.commons.core.services.ResourceResolverService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.List;

@Component(service = { AssetService.class },
  property = {
    Constants.SERVICE_DESCRIPTION +  "=Asset Service"
})
public class AssetServiceImpl implements AssetService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ResourceResolverService resolverService;

    @Reference
    private Replicator replicator;

    /**
     * Deletes multiple assets at the given paths.
     *
     * @param assetPaths the paths of the assets to delete
     * @return true if all assets were deleted successfully, false otherwise
     */
    @Override
    public boolean deleteAssets(List<String> assetPaths) {
        if (assetPaths == null || assetPaths.isEmpty()) {
            LOG.debug("No assets to be deleted");
            return true;
        }

        boolean success = true;
        ResourceResolver resolver = resolverService.getResourceResolver();

        if (resolver != null) {
            Session session = resolver.adaptTo(Session.class);

            if (session != null) {
                try {
                    for (String assetPath : assetPaths) {
                        Resource resource = resolver.getResource(assetPath);
                        if (resource != null) {
                            resolver.delete(resource);
                            replicator.replicate(session, ReplicationActionType.DEACTIVATE, assetPath);
                        }
                    }
                } catch (PersistenceException | ReplicationException ex) {
                    success = false;
                    LOG.error("Error deleting assets: {}", ex.getMessage());
                } finally {
                    if (resolver.isLive()) {
                        try {
                            resolver.commit();
                            session.save();
                        } catch (PersistenceException | RepositoryException ex) {
                            success = false;
                            LOG.error("Error committing transaction: {}", ex.getMessage());
                        }
                        resolver.close();
                    }
                }
            }
        }

        return success;
    }
}
