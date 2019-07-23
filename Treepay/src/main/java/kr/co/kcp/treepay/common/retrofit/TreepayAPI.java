package kr.co.kcp.treepay.common.retrofit;

import kr.co.kcp.treepay.common.model.CardBinModel;
import kr.co.kcp.treepay.common.model.CardBinRequest;
import kr.co.kcp.treepay.common.model.CardDeleteModel;
import kr.co.kcp.treepay.common.model.CardDeleteRequest;
import kr.co.kcp.treepay.common.model.CardListModel;
import kr.co.kcp.treepay.common.model.CardListRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import kr.co.kcp.treepay.common.model.GetOttModel;
import kr.co.kcp.treepay.common.model.GetOttRequest;

/**
 * Created by jhlee on 2015-02-25.
 */
public interface TreepayAPI
{
    @Headers("Content-Type: application/json")
    @POST("/api/ca/getOtt.api")
    Call<GetOttModel> getCaOtt(@Body GetOttRequest body);

    @Headers("Content-Type: application/json")
    @POST("/api/oc/getOtt.api")
    Call<GetOttModel> getOcOtt(@Body GetOttRequest body);

    @Headers("Content-Type: application/json")
    @POST("/api/cardBin.api")
    Call<CardBinModel> cardBin(@Body CardBinRequest body);

    @Headers("Content-Type: application/json")
    @POST("/api/cardList.api")
    Call<CardListModel> cardList(@Body CardListRequest body);

    @Headers("Content-Type: application/json")
    @POST("/api/cardDelete.api")
    Call<CardDeleteModel> cardDelete(@Body CardDeleteRequest body);
}
