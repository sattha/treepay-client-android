package kr.co.kcp.treepay.common.model;


import java.io.Serializable;

public class CardBinModel implements Serializable
{
    private static final long serialVersionUID = -1235564412334233472L;

    private String res_msg;
    private String card_kind;
    private String traceNo;
    private String promotion_code;
    private String issue_national;
    private String res_cd;
    private String card_bin_no;
    private String site_cd;
    private String tp_langFlag;
    private String card_brand_name;
    private Object issue_bank;
    private String card_Type;

    public String getRes_msg()
    {
        return res_msg;
    }

    public void setRes_msg(String res_msg)
    {
        this.res_msg = res_msg;
    }

    public String getCard_kind()
    {
        return card_kind;
    }

    public void setCard_kind(String card_kind)
    {
        this.card_kind = card_kind;
    }

    public String getTraceNo()
    {
        return traceNo;
    }

    public void setTraceNo(String traceNo)
    {
        this.traceNo = traceNo;
    }

    public String getPromotion_code()
    {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code)
    {
        this.promotion_code = promotion_code;
    }

    public String getIssue_national()
    {
        return issue_national;
    }

    public void setIssue_national(String issue_national)
    {
        this.issue_national = issue_national;
    }

    public String getRes_cd()
    {
        return res_cd;
    }

    public void setRes_cd(String res_cd)
    {
        this.res_cd = res_cd;
    }

    public String getCard_bin_no()
    {
        return card_bin_no;
    }

    public void setCard_bin_no(String card_bin_no)
    {
        this.card_bin_no = card_bin_no;
    }

    public String getSite_cd()
    {
        return site_cd;
    }

    public void setSite_cd(String site_cd)
    {
        this.site_cd = site_cd;
    }

    public String getTp_langFlag()
    {
        return tp_langFlag;
    }

    public void setTp_langFlag(String tp_langFlag)
    {
        this.tp_langFlag = tp_langFlag;
    }

    public String getCard_brand_name()
    {
        return card_brand_name;
    }

    public void setCard_brand_name(String card_brand_name)
    {
        this.card_brand_name = card_brand_name;
    }

    public Object getIssue_bank()
    {
        return issue_bank;
    }

    public void setIssue_bank(Object issue_bank)
    {
        this.issue_bank = issue_bank;
    }

    public String getCard_Type()
    {
        return card_Type;
    }

    public void setCard_Type(String card_Type)
    {
        this.card_Type = card_Type;
    }
}