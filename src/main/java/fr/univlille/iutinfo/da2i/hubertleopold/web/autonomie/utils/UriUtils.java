package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils;

import org.apache.commons.text.StringEscapeUtils;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {

    public static URI buildUriFromExistingSetParameters(String existing, String query) throws URISyntaxException {
        URI referrerUri = new URI(existing);
        return new URI(referrerUri.getScheme(), referrerUri.getUserInfo(), referrerUri.getHost(), referrerUri.getPort(), referrerUri.getPath(), query, referrerUri.getFragment());
    }

    public static URI buildResponseUri(String cameFrom, String query, String message, String type) throws URISyntaxException {
        String protectedMessage = StringEscapeUtils.escapeHtml4(message);
        String protectedType = StringEscapeUtils.escapeHtml4(type);
        return buildUriFromExistingSetParameters(cameFrom, query+"&message="+protectedMessage+"&errortype="+protectedType);
    }

}
