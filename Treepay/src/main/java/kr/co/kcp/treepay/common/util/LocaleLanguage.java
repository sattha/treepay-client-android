package kr.co.kcp.treepay.common.util;

import java.util.Locale;

/**
 * Created by hhan on 2018. 1. 29..
 */

public class LocaleLanguage
{
    public static String getLanguage()
    {
        if("th".equals(Locale.getDefault().getLanguage()))
        {
            return Locale.getDefault().getLanguage();
        }
        else
        {
            return "en";
        }
    }
}
