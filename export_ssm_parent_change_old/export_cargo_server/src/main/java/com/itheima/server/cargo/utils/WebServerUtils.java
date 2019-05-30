package com.itheima.server.cargo.utils;

import com.itheima.domain.vo.ExportResult;
import com.itheima.domain.vo.ExportVo;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

public class WebServerUtils {

    private static String WEB_SERVER_URL = "http://localhost:8081/ws/export/user";

    private static String MEDIA_TYPE = MediaType.APPLICATION_XML;

    public static void setWebServerUrl(String webServerUrl) {
        WEB_SERVER_URL = webServerUrl;
    }

    public static void setMediaType(String mediaType) {
        MEDIA_TYPE = mediaType;
    }

    public static void saveExportByWebServer(ExportVo exportVo){
        WebClient.create(WEB_SERVER_URL).type(MEDIA_TYPE).encoding("UTF-8").post(exportVo);
    }

    public static ExportResult getExportResult(String id){
        ExportResult exportResult = WebClient.create(WEB_SERVER_URL + "/" + id).type(MEDIA_TYPE).encoding("utf-8").get(ExportResult.class);
        return exportResult;
    }
}
