package kr.co.kcp.treepay.common.model;
import java.io.Serializable;

public class GetOttModel implements Serializable
{
    private static final long serialVersionUID = -1235564412122233472L;
    private String res_msg;
    private String res_cd;
    private String ott;

    public String getRes_msg()
    {
        return res_msg;
    }

    public void setRes_msg(String res_msg)
    {
        this.res_msg = res_msg;
    }

    public String getRes_cd()
    {
        return res_cd;
    }

    public void setRes_cd(String res_cd)
    {
        this.res_cd = res_cd;
    }

    public String getOtt()
    {
        return ott;
    }

    public void setOtt(String ott)
    {
        this.ott = ott;
    }
}
