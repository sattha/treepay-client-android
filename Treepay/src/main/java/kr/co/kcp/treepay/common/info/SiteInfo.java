package kr.co.kcp.treepay.common.info;

/**
 * Created by hhan on 2018. 1. 23..
 */

public class SiteInfo
{
    private String siteCd;
    private String userId;
    private String octInit3d;
    private String ott3d;
    private String oct3d;
    private String octYn;

    public String getSiteCd()
    {
        return siteCd;
    }

    public void setSiteCd(String siteCd)
    {
        this.siteCd = siteCd;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getOctInit3d()
    {
        return octInit3d;
    }

    public void setOctInit3d(String octInit3d)
    {
        this.octInit3d = octInit3d;
    }

    public String getOtt3d()
    {
        return ott3d;
    }

    public void setOtt3d(String ott3d)
    {
        this.ott3d = ott3d;
    }

    public String getOct3d()
    {
        return oct3d;
    }

    public void setOct3d(String oct3d)
    {
        this.oct3d = oct3d;
    }

    public String getOctYn()
    {
        return octYn;
    }

    public void setOctYn(String octYn)
    {
        this.octYn = octYn;
    }

    private static SiteInfo instance = null;

    public static synchronized SiteInfo getInstance()
    {
        if (null == instance)
        {
            instance = new SiteInfo();
        }
        return instance;
    }
}
