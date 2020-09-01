package application.util;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

public class UrlUtil {
    public static String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();

        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }

        System.out.println(enc + "******************************1");

        pathSegment = UriUtils.encodePathSegment(pathSegment, enc);

        System.out.println(pathSegment + "******************************2");

        return pathSegment;
    }
}
