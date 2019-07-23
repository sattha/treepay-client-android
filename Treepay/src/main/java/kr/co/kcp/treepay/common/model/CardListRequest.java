package kr.co.kcp.treepay.common.model;
import java.io.Serializable;

public class CardListRequest implements Serializable
{
    private static final long serialVersionUID = -1123164213122233472L;
    private String site_cd;
    private String user_id;
    private String tp_langFlag;

    public String getSite_cd()
    {
        return site_cd;
    }

    public void setSite_cd(String site_cd)
    {
        this.site_cd = site_cd;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getTp_langFlag()
    {
        return tp_langFlag;
    }

    public void setTp_langFlag(String tp_langFlag)
    {
        this.tp_langFlag = tp_langFlag;
    }
}
