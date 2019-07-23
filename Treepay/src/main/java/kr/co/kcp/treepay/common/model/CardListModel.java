package kr.co.kcp.treepay.common.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardListModel implements Serializable
{
    private static final long serialVersionUID = -1235564412354433472L;

    @SerializedName("oct_init_3d")
    @Expose
    private String octInit3d;
    @SerializedName("ott_3d")
    @Expose
    private String ott3d;
    @SerializedName("oct_3d")
    @Expose
    private String oct3d;
    @SerializedName("res_msg")
    @Expose
    private String resMsg;
    @SerializedName("oct_yn")
    @Expose
    private String octYn;
    @SerializedName("card_list")
    @Expose
    private List<card_list> cardlist = null;
    @SerializedName("card_count")
    @Expose
    private Integer cardCount;
    @SerializedName("res_cd")
    @Expose
    private String resCd;
    @SerializedName("auto_refresh")
    @Expose
    private long autoRefresh;

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

    public String getResMsg()
    {
        return resMsg;
    }

    public void setResMsg(String resMsg)
    {
        this.resMsg = resMsg;
    }

    public String getOctYn()
    {
        return octYn;
    }

    public void setOctYn(String octYn)
    {
        this.octYn = octYn;
    }

    public List<card_list> getCardlist()
    {
        return cardlist;
    }

    public void setCardlist(List<card_list> cardlist)
    {
        this.cardlist = cardlist;
    }

    public Integer getCardCount()
    {
        return cardCount;
    }

    public void setCardCount(Integer cardCount)
    {
        this.cardCount = cardCount;
    }

    public String getResCd()
    {
        return resCd;
    }

    public void setResCd(String resCd)
    {
        this.resCd = resCd;
    }

    public long getAutoRefresh()
    {
        return autoRefresh;
    }

    public void setAutoRefresh(long autoRefresh)
    {
        this.autoRefresh = autoRefresh;
    }

    public class card_list implements Serializable
    {
        private static final long serialVersionUID = -1998564412354433472L;

        private String oct;
        private String expiration_mmyy;
        private String card_brand;
        private String card_last_num;

        private boolean check;

        private boolean lastPosition;

        public String getOct()
        {
            return oct;
        }

        public void setOct(String oct)
        {
            this.oct = oct;
        }

        public String getExpiration_mmyy()
        {
            return expiration_mmyy;
        }

        public void setExpiration_mmyy(String expiration_mmyy)
        {
            this.expiration_mmyy = expiration_mmyy;
        }

        public String getCard_brand()
        {
            return card_brand;
        }

        public void setCard_brand(String card_brand)
        {
            this.card_brand = card_brand;
        }

        public String getCard_last_num()
        {
            return card_last_num;
        }

        public void setCard_last_num(String card_last_num)
        {
            this.card_last_num = card_last_num;
        }

        public boolean isCheck()
        {
            return check;
        }

        public void setCheck(boolean check)
        {
            this.check = check;
        }

        public boolean isLastPosition()
        {
            return lastPosition;
        }

        public void setLastPosition(boolean lastPosition)
        {
            this.lastPosition = lastPosition;
        }
    }
}