package kr.co.kcp.treepay.common.android.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.WindowManager;

import java.io.Serializable;

import kr.co.kcp.treepay.common.R;
import kr.co.kcp.treepay.common.info.CommonInfo;
import kr.co.kcp.treepay.common.info.ProductInfo;
import kr.co.kcp.treepay.common.info.SiteInfo;
import kr.co.kcp.treepay.common.model.CardListModel;
import kr.co.kcp.treepay.common.model.CardListRequest;
import kr.co.kcp.treepay.common.retrofit.ApiClient;
import kr.co.kcp.treepay.common.retrofit.TreepayAPI;
import kr.co.kcp.treepay.common.util.LocaleLanguage;
import kr.co.kcp.treepay.common.widget.ProgressWheelDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TreepayActivity extends Activity
{
    ProgressWheelDialog progressWheelDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treepay);
        progressWheelDialog = new ProgressWheelDialog(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(ProductInfo.getInstance().getTotalAmount().indexOf(".") > -1)
        {
            if(ProductInfo.getInstance().getTotalAmount().length() - ProductInfo.getInstance().getTotalAmount().indexOf(".") > 3)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        TreepayActivity.this);
                alertDialogBuilder
                        .setTitle(R.string.error_title)
                        .setMessage(getString(R.string.invalid_amount))
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("error_msg", getString(R.string.invalid_amount));
                                        setResult(Activity.RESULT_CANCELED, returnIntent);
                                        finish();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else
            {
                cardList();
            }
        }
        else
        {
            cardList();
        }

    }

    private void cardList()
    {
        progressWheelDialog.show();
        TreepayAPI treepayAPI = ApiClient.getClient().create(TreepayAPI.class);

        SiteInfo.getInstance().setOct3d("N");
        SiteInfo.getInstance().setOtt3d("N");
        SiteInfo.getInstance().setOctInit3d("N");
        SiteInfo.getInstance().setOctYn("N");

        CardListRequest cardListRequest = new CardListRequest();
        cardListRequest.setSite_cd(SiteInfo.getInstance().getSiteCd());
        cardListRequest.setUser_id(SiteInfo.getInstance().getUserId());
        cardListRequest.setTp_langFlag(LocaleLanguage.getLanguage());
        treepayAPI.cardList(cardListRequest)
                .enqueue(new Callback<CardListModel>()
                {
                    @Override
                    public void onResponse(Call<CardListModel> call, Response<CardListModel> response)
                    {
                        progressWheelDialog.dismiss();
                        CardListModel model = response.body();
                        if (model != null)
                        {
                            if("0000".equals(model.getResCd()))
                            {
                                SiteInfo.getInstance().setOct3d(model.getOct3d());
                                SiteInfo.getInstance().setOtt3d(model.getOtt3d());
                                SiteInfo.getInstance().setOctInit3d(model.getOctInit3d());
                                SiteInfo.getInstance().setOctYn(model.getOctYn());
                                if(model.getCardCount() > 0 && "Y".equals(model.getOctYn()))
                                {
                                    Intent intent = new Intent(TreepayActivity.this, TreepayCreditCardListActivity.class);
                                    intent.putExtra("CardVoList", (Serializable) model.getCardlist());
                                    startActivityForResult(intent, CommonInfo.OCT_REQUEST_CODE);
                                }
                                else
                                {
                                    Intent intent = new Intent(TreepayActivity.this, TreepayCreditCardActivity.class);
                                    startActivityForResult(intent, CommonInfo.OCT_REQUEST_CODE);
                                }
                            }
                            else
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        TreepayActivity.this);
                                alertDialogBuilder
                                        .setTitle(R.string.error_title)
                                        .setMessage("[" + model.getResCd() +"]"+ "\n" + model.getResMsg())
                                        .setPositiveButton(R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        finish();
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
                                {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface)
                                    {
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                        else
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    TreepayActivity.this);
                            alertDialogBuilder
                                    .setTitle(R.string.error_title)
                                    .setMessage(getString(R.string.error))
                                    .setPositiveButton(R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
                            {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface)
                                {
                                    finish();
                                }
                            });
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CardListModel> call, Throwable t)
                    {
                        progressWheelDialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                TreepayActivity.this);
                        alertDialogBuilder
                                .setTitle(R.string.error_title)
                                .setMessage(t.getLocalizedMessage() + "\n" + getString(R.string.error))
                                .setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
                        {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface)
                            {
                                finish();
                            }
                        });
                        alertDialog.show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CommonInfo.OCT_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(CommonInfo.OTT, data.getStringExtra(CommonInfo.OTT));
                returnIntent.putExtra(CommonInfo.OCT, data.getStringExtra(CommonInfo.OCT));
                returnIntent.putExtra(CommonInfo.CVV, data.getStringExtra(CommonInfo.CVV));
                returnIntent.putExtra(CommonInfo.SAVE_CARD, data.getBooleanExtra(CommonInfo.SAVE_CARD, false));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            else
            {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }
    }
}
