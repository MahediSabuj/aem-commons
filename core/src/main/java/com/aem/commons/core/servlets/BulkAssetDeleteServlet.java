package com.aem.commons.core.servlets;

import com.aem.commons.core.jobs.BulkAssetDeleteJob;
import com.aem.commons.core.services.ExcelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/public/aem-commons/bulk-asset-delete")
public class BulkAssetDeleteServlet extends SlingAllMethodsServlet {

    @Reference
    private ExcelService excelService;

    @Reference
    private JobManager jobManager;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        List<String> unusedAssets = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        Part filePart = request.getPart("file");

        try (InputStream inputStream = filePart.getInputStream()) {
            unusedAssets = excelService.readExcelFile(inputStream);

            Map<String, Object> properties = Map.of(
                "unusedAssets", mapper.writeValueAsString(unusedAssets)
            );

            jobManager.createJob(BulkAssetDeleteJob.TOPIC)
                .properties(properties)
                .add();
        }

        String jsonResponse = mapper.writeValueAsString(unusedAssets);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
