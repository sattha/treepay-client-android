package kr.co.kcp.treepay.common.info;

/**
 * Created by hhan on 2018. 1. 23..
 */

public class ProductInfo
{
    private String productName;
    private String totalAmount;

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    private static ProductInfo instance = null;

    public static synchronized ProductInfo getInstance()
    {
        if (null == instance)
        {
            instance = new ProductInfo();
        }
        return instance;
    }
}
