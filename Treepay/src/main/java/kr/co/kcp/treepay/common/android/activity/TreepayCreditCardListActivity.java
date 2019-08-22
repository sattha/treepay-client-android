package kr.co.kcp.treepay.common.android.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;

import kr.co.kcp.treepay.common.BuildConfig;
import kr.co.kcp.treepay.common.R;
import kr.co.kcp.treepay.common.android.adapter.CardRecyclerViewAdapter;
import kr.co.kcp.treepay.common.info.CommonInfo;
import kr.co.kcp.treepay.common.info.ProductInfo;
import kr.co.kcp.treepay.common.info.SiteInfo;
import kr.co.kcp.treepay.common.model.CardDeleteModel;
import kr.co.kcp.treepay.common.model.CardDeleteRequest;
import kr.co.kcp.treepay.common.model.CardListModel;
import kr.co.kcp.treepay.common.model.CardListRequest;
import kr.co.kcp.treepay.common.retrofit.ApiClient;
import kr.co.kcp.treepay.common.retrofit.TreepayAPI;
import kr.co.kcp.treepay.common.util.LocaleLanguage;
import kr.co.kcp.treepay.common.widget.ProgressWheelDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreepayCreditCardListActivity extends Activity {
    View backView;
    ProgressWheelDialog progressWheelDialog;
    RecyclerView recyclerView;
    EditText cvvEditText;
    CardRecyclerViewAdapter cardRecyclerViewAdapter;
    private ArrayList<CardListModel.card_list> cardlistList;
    int lastPosition = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView cardNumberTextView;
    Button cardDeleteButton;
    Button submitButton;
    Button newCardButton;
    TextView productNameTextView;
    TextView totalAmountTextView;

    NestedScrollView mainScrollView;

    private long currentTime = 0;
    private static final int REFRESH_LIMIT_TIME = 0;

    boolean addCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treepay_credit_card_list);

        mainScrollView = findViewById(R.id.mainScrollView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productNameTextView.setText(ProductInfo.getInstance().getProductName());
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        totalAmountTextView.setText(String.format("%,.2f", Double.parseDouble(ProductInfo.getInstance().getTotalAmount())) + " THB");

        cardNumberTextView = findViewById(R.id.cardNumberTextView);

        cardlistList = (ArrayList<CardListModel.card_list>) getIntent().getSerializableExtra("CardVoList");
        initCardList();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_normal);

        cvvEditText = findViewById(R.id.cvvEditText);


        backView = findViewById(R.id.backView);
        backView.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cardRecyclerViewAdapter = new CardRecyclerViewAdapter(this, cardlistList);
        cardRecyclerViewAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<CardListModel.card_list>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, CardListModel.card_list cardlist, int position) {
                if (position != lastPosition) {
                    cardlistList.get(lastPosition).setCheck(false);
                    cardlistList.get(position).setCheck(true);
                    cardRecyclerViewAdapter.notifyDataSetChanged();
                    cardNumberTextView.setText("****-****-****" + cardlist.getCard_last_num()
                            + "(" + cardlist.getExpiration_mmyy().substring(0, 2) + " / " + cardlist.getExpiration_mmyy().substring(2, 4) + ")");


                }
                lastPosition = position;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, CardListModel.card_list cardlist, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(cardRecyclerViewAdapter);
        progressWheelDialog = new ProgressWheelDialog(this);

        swipeRefreshLayout.setOnRefreshListener(() -> cardList());

        cardDeleteButton = findViewById(R.id.cardDeleteButton);
        cardDeleteButton.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    TreepayCreditCardListActivity.this);
            alertDialogBuilder
                    .setMessage(getString(R.string.confirm_delete))
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(getString(R.string.ok),
                            (dialog, id) -> cardDelete());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        newCardButton = findViewById(R.id.newCardButton);
        newCardButton.setOnClickListener(view -> {
            Intent intent = new Intent(TreepayCreditCardListActivity.this, TreepayCreditCardActivity.class);
            startActivityForResult(intent, CommonInfo.OCT_REQUEST_CODE);
        });


        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            if (cvvEditText.getText().toString().length() < 3) {
                scrollViewDown();
                cvvEditText.setError(getString(R.string.valid_cvv));
                cvvEditText.requestFocus();
                return;
            }
            if (cardlistList != null) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(CommonInfo.OCT, cardlistList.get(lastPosition).getOct());
                returnIntent.putExtra(CommonInfo.CVV, cvvEditText.getText().toString());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        refreshTimerStop();
    }

    private void refreshTimerStart() {
        limitTimeHandler.sendEmptyMessage(REFRESH_LIMIT_TIME);
    }

    private void refreshTimerStop() {
        limitTimeHandler.removeMessages(REFRESH_LIMIT_TIME);
    }

    private Handler limitTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_LIMIT_TIME) {
                calcLimitTime();
            }
        }
    };

    private void calcLimitTime() {
        if (currentTime > 0) {
            currentTime--;
            if (limitTimeHandler != null) {
                limitTimeHandler.sendEmptyMessageDelayed(REFRESH_LIMIT_TIME, 1000);
            }
        } else {
            cardList();
        }
    }

    private void initCardList() {
        cardlistList.get(0).setCheck(true);
        cardNumberTextView.setText("****-****-****" + cardlistList.get(0).getCard_last_num()
                + "(" + cardlistList.get(0).getExpiration_mmyy().substring(0, 2) + " / " + cardlistList.get(0).getExpiration_mmyy().substring(2, 4) + ")");
        cardlistList.get(cardlistList.size() - 1).setLastPosition(true);
    }

    private void cardList() {
        swipeRefreshLayout.setRefreshing(true);
        TreepayAPI treepayAPI = ApiClient.getClient().create(TreepayAPI.class);

        CardListRequest cardListRequest = new CardListRequest();
        cardListRequest.setSite_cd(SiteInfo.getInstance().getSiteCd());
        cardListRequest.setUser_id(SiteInfo.getInstance().getUserId());
        treepayAPI.cardList(cardListRequest)
                .enqueue(new Callback<CardListModel>() {
                    @Override
                    public void onResponse(Call<CardListModel> call, Response<CardListModel> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        CardListModel model = response.body();
                        if (model != null) {
                            if ("0000".equals(model.getResCd())) {
                                if (model.getCardCount() > 0) {
                                    lastPosition = 0;
                                    cardlistList.clear();
                                    cardlistList.addAll(model.getCardlist());
                                    initCardList();
                                    cardRecyclerViewAdapter.notifyDataSetChanged();
                                    if (model.getAutoRefresh() >= 30) {
                                        refreshTimerStop();
                                        currentTime = model.getAutoRefresh();
                                        refreshTimerStart();
                                    }
                                } else {
                                    if (!addCancel) {
                                        Intent intent = new Intent(TreepayCreditCardListActivity.this, TreepayCreditCardActivity.class);
                                        startActivityForResult(intent, CommonInfo.OCT_REQUEST_CODE);
                                    } else {
                                        finish();
                                    }
                                }
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        TreepayCreditCardListActivity.this);
                                alertDialogBuilder
                                        .setTitle(R.string.error_title)
                                        .setMessage("[" + model.getResCd() + "]" + "\n" + model.getResMsg())
                                        .setPositiveButton(R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    TreepayCreditCardListActivity.this);
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
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CardListModel> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                TreepayCreditCardListActivity.this);
                        alertDialogBuilder
                                .setTitle(R.string.error_title)
                                .setMessage(t.getLocalizedMessage() + "\n" + getString(R.string.error))
                                .setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                            }
                                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
    }

    private void cardDelete() {
        swipeRefreshLayout.setRefreshing(true);
        TreepayAPI treepayAPI = ApiClient.getClient().create(TreepayAPI.class);

        CardDeleteRequest cardDeleteRequest = new CardDeleteRequest();
        cardDeleteRequest.setSite_cd(SiteInfo.getInstance().getSiteCd());
        cardDeleteRequest.setUser_id(SiteInfo.getInstance().getUserId());
        cardDeleteRequest.setOct(cardlistList.get(lastPosition).getOct());
        cardDeleteRequest.setVer(BuildConfig.TREEPAY_API_VERSION);
        cardDeleteRequest.setTp_langFlag(LocaleLanguage.getLanguage());
        treepayAPI.cardDelete(cardDeleteRequest)
                .enqueue(new Callback<CardDeleteModel>() {
                    @Override
                    public void onResponse(Call<CardDeleteModel> call, Response<CardDeleteModel> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        CardDeleteModel model = response.body();
                        if (model != null) {
                            if ("0000".equals(model.getRes_cd())) {
                                addCancel = false;
                                cardList();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        TreepayCreditCardListActivity.this);
                                alertDialogBuilder
                                        .setTitle(R.string.error_title)
                                        .setMessage("[" + model.getRes_cd() + "]" + "\n" + getString(R.string.error))
                                        .setPositiveButton(R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    TreepayCreditCardListActivity.this);
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
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CardDeleteModel> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                TreepayCreditCardListActivity.this);
                        alertDialogBuilder
                                .setTitle(R.string.error_title)
                                .setMessage(t.getLocalizedMessage() + "\n" + getString(R.string.error))
                                .setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                            }
                                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonInfo.OCT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(CommonInfo.OTT, data.getStringExtra(CommonInfo.OTT));
                returnIntent.putExtra(CommonInfo.CVV, data.getStringExtra(CommonInfo.CVV));
                returnIntent.putExtra(CommonInfo.SAVE_CARD, data.getBooleanExtra(CommonInfo.SAVE_CARD, false));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                addCancel = true;
            }
        }
    }

    private void scrollViewDown() {
        View lastChild = mainScrollView.getChildAt(mainScrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + mainScrollView.getPaddingBottom();
        int sy = mainScrollView.getScrollY();
        int sh = mainScrollView.getHeight();
        int delta = bottom - (sy + sh);

        mainScrollView.smoothScrollBy(0, delta);
    }
}
