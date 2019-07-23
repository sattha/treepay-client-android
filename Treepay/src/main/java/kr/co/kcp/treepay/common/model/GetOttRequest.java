package kr.co.kcp.treepay.common.model;
import java.io.Serializable;

public class GetOttRequest implements Serializable
{
    private static final long serialVersionUID = -1123556412122233472L;
    private String site_cd;
    private String pay_type;
    private String expiration_yy;
    private String expiration_mm;
    private String card_number;
    private String last_card_holder_name;
    private String first_card_holder_name;
    private String ver;
    private String tp_langFlag;

    public String getSite_cd()
    {
        return site_cd;
    }

    public void setSite_cd(String site_cd)
    {
        this.site_cd = site_cd;
    }

    public String getPay_type()
    {
        return pay_type;
    }

    public void setPay_type(String pay_type)
    {
        this.pay_type = pay_type;
    }

    public String getExpiration_yy()
    {
        return expiration_yy;
    }

    public void setExpiration_yy(String expiration_yy)
    {
        this.expiration_yy = expiration_yy;
    }

    public String getExpiration_mm()
    {
        return expiration_mm;
    }

    public void setExpiration_mm(String expiration_mm)
    {
        this.expiration_mm = expiration_mm;
    }

    public String getCard_number()
    {
        return card_number;
    }

    public void setCard_number(String card_number)
    {
        this.card_number = card_number;
    }

    public String getLast_card_holder_name()
    {
        return last_card_holder_name;
    }

    public void setLast_card_holder_name(String last_card_holder_name)
    {
        this.last_card_holder_name = last_card_holder_name;
    }

    public String getFirst_card_holder_name()
    {
        return first_card_holder_name;
    }

    public void setFirst_card_holder_name(String first_card_holder_name)
    {
        this.first_card_holder_name = first_card_holder_name;
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
