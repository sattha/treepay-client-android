package kr.co.kcp.treepay.common.model;
import java.io.Serializable;

public class CardBinRequest implements Serializable
{
    private static final long serialVersionUID = -1123164213122233472L;
    private String card_bin_no;
    private String ver;
    private String tp_langFlag;

    public String getCard_bin_no()
    {
        return card_bin_no;
    }

    public void setCard_bin_no(String card_bin_no)
    {
        this.card_bin_no = card_bin_no;
    }

    public String getVer()
    {
        return ver;
    }

    public void setVer(String ver)
    {
        this.ver = ver;
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
