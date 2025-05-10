package com.aem.commons.core.jobs;

import com.aem.commons.core.services.AssetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component(service = { JobConsumer.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Bulk Asset Delete Job",
    JobConsumer.PROPERTY_TOPICS + "=" + BulkAssetDeleteJob.TOPIC
  })
public class BulkAssetDeleteJob implements JobConsumer {
    public final static String TOPIC = "topic/bulk-asset/delete";

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Reference
    private AssetService assetService;

    @Override
    public JobResult process(Job job) {
        String unusedAssets = job.getProperty("unusedAssets", String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<String> assetPaths = mapper.readValue(unusedAssets, new TypeReference<>() {});

            boolean success = assetService.deleteAssets(assetPaths);
            return success ? JobResult.OK : JobResult.FAILED;

        } catch (JsonProcessingException ex) {
            LOG.error("Error processing JSON: {}", ex.getMessage());
        }

        return JobResult.FAILED;
    }
}
