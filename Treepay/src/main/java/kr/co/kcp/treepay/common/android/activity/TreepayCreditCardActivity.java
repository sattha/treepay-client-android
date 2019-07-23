package kr.co.kcp.treepay.common.android.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.regex.Pattern;

import kr.co.kcp.treepay.common.BuildConfig;
import kr.co.kcp.treepay.common.R;
import kr.co.kcp.treepay.common.info.CommonInfo;
import kr.co.kcp.treepay.common.info.ProductInfo;
import kr.co.kcp.treepay.common.info.SiteInfo;
import kr.co.kcp.treepay.common.model.CardBinModel;
import kr.co.kcp.treepay.common.model.CardBinRequest;
import kr.co.kcp.treepay.common.model.GetOttModel;
import kr.co.kcp.treepay.common.model.GetOttRequest;
import kr.co.kcp.treepay.common.retrofit.ApiClient;
import kr.co.kcp.treepay.common.retrofit.TreepayAPI;
import kr.co.kcp.treepay.common.util.LocaleLanguage;
import kr.co.kcp.treepay.common.widget.ProgressWheelDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreepayCreditCardActivity extends Activity
{
    View backView;
    EditText cardNumberEditText;
    EditText expirationEditText;
    EditText cvvEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    ImageView cardCompanyImageView;
    TextView saveCardTextView;
    CheckBox saveCardCheckBox;
    ProgressWheelDialog progressWheelDialog;
    ScalableLayout saveCardInfoScalableLayout;
    ScalableLayout saveCardInfo2ScalableLayout;
    Button submitButton;
    TextView productNameTextView;
    TextView totalAmountTextView;


    String cardNumber;
    String expiration;

    ScrollView mainScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treepay_credit_card);

        mainScrollView = findViewById(R.id.mainScrollView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productNameTextView.setText(ProductInfo.getInstance().getProductName());
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        totalAmountTextView.setText(String.format("%,.2f", Double.parseDouble(ProductInfo.getInstance().getTotalAmount())) + " THB");

        backView = findViewById(R.id.backView);
        backView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expirationEditText = findViewById(R.id.expirationEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        firstNameEditText.setFilters(new InputFilter[] {filter});
        lastNameEditText = findViewById(R.id.lastNameEditText);
        lastNameEditText.setFilters(new InputFilter[] {filter});
        cardCompanyImageView = findViewById(R.id.cardCompanyImageView);
        saveCardTextView = findViewById(R.id.saveCardTextView);
        saveCardCheckBox = findViewById(R.id.saveCardCheckBox);

        saveCardInfoScalableLayout = findViewById(R.id.saveCardInfoScalableLayout);
        saveCardInfo2ScalableLayout = findViewById(R.id.saveCardInfo2ScalableLayout);
        if("Y".equals(SiteInfo.getInstance().getOctYn()))
        {
            saveCardInfoScalableLayout.setVisibility(View.VISIBLE);
            saveCardInfo2ScalableLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            saveCardInfoScalableLayout.setVisibility(View.GONE);
            saveCardInfo2ScalableLayout.setVisibility(View.GONE);
        }

        cardNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cardNumberEditText.setSelection(cardNumberEditText.getText().length());
                }
            }
        });

        cardNumberEditText.addTextChangedListener(new TextWatcher()
        {

            private int previousLength;
            private boolean backSpace;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                previousLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(editable.length() > 0)
                {
                    backSpace = previousLength > editable.length();

                    if (backSpace)
                    {
                        if(editable.length()%5 == 0)
                        {
                            cardNumberEditText.setText((cardNumberEditText.getText().toString()).substring(0, editable.length()-1));
                            cardNumberEditText.setSelection(cardNumberEditText.getText().length());
                        }

                        if(cardNumberEditText.length() == 6)
                        {
                            cardCompanyImageView.setImageResource(R.drawable.card_nor);
                        }
                    }
                    else
                    {
                        int length = editable.length();
                        if(editable.length() > 0)
                        {
                            backSpace = previousLength > cardNumberEditText.getText().length();

                            if (!backSpace)
                            {
                                cardNumberEditText.setTypeface(null, Typeface.BOLD);
                                if(length%5 == 0 && length < 16)
                                {
                                    cardNumberEditText.setText(editable.toString().substring(0, length - 1)
                                            + " " + editable.toString().substring(length - 1, length));
                                    previousLength = previousLength + 2;
                                    cardNumberEditText.setSelection(cardNumberEditText.getText().length());
                                }

                                if(editable.length() == 7)
                                {
                                    cardBin(editable.toString().replace(" ", ""));
                                }
                            }
                        }
                        else
                        {
                            cardNumberEditText.setTypeface(null, Typeface.NORMAL);
                        }
                    }
                }
            }
        });

        expirationEditText.addTextChangedListener(new TextWatcher()
        {
            private int previousLength;
            private boolean backSpace;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                previousLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length() > 0)
                {
                    backSpace = previousLength > expirationEditText.getText().length();

                    if (!backSpace)
                    {
                        expirationEditText.setTypeface(null, Typeface.BOLD);
                        if(charSequence.length() == 3)
                        {
                            expirationEditText.setText(expirationEditText.getText().toString().substring(0,2) + " / " + expirationEditText.getText().toString().substring(2,3));
                            expirationEditText.setSelection(expirationEditText.getText().length());
                        }

                        if(charSequence.length() == 2)
                        {
                            if(Integer.parseInt(expirationEditText.getText().toString().substring(0,2)) > 12)
                            {
                                expirationEditText.setError(getString(R.string.valid_month));
                                expirationEditText.setText("");
                            }
                        }
                    }
                }
                else
                {
                    expirationEditText.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                backSpace = previousLength > editable.length();

                if (backSpace) {
                    if(editable.length() == 5)
                    {
                        expirationEditText.setText((expirationEditText.getText().toString()).substring(0, 2));
                        expirationEditText.setSelection(expirationEditText.getText().length());
                    }
                }
            }
        });

        firstNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                scrollViewDown();
            }
        });

        firstNameEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length() > 0)
                {
                    firstNameEditText.setTypeface(null, Typeface.BOLD);
                }
                else
                {
                    firstNameEditText.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        lastNameEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length() > 0)
                {
                    lastNameEditText.setTypeface(null, Typeface.BOLD);
                }
                else
                {
                    lastNameEditText.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        progressWheelDialog = new ProgressWheelDialog(this);

        saveCardTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(saveCardCheckBox.isChecked())
                {
                    saveCardCheckBox.setChecked(false);
                }
                else
                {
                    saveCardCheckBox.setChecked(true);
                }
            }
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(creditCardValidCheck())
                {
                    getOtt();
                }
            }
        });
    }

    protected InputFilter filter = new InputFilter()
    {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend)
        {
            Pattern ps = Pattern.compile("^[a-zA-Z]+|\\s$");
            if (!ps.matcher(source).matches())
            {
                return "";

            }
            return null;
        }
    };

    private boolean creditCardValidCheck()
    {
        cardNumber = cardNumberEditText.getText().toString().replace(" ", "");
        if(TextUtils.isEmpty(cardNumber) || cardNumber.length() < 15)
        {
            Toast.makeText(this, kr.co.kcp.treepay.common.R.string.valid_card_number, Toast.LENGTH_LONG).show();
            return false;
        }

        expiration = expirationEditText.getText().toString().replace(" / ", "");
        if(TextUtils.isEmpty(expiration) || expiration.length() < 4)
        {
            expirationEditText.setError(getString(R.string.valid_month));
            expirationEditText.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(cvvEditText.getText().toString()) || cvvEditText.getText().toString().length() < 3)
        {
            scrollViewDown();
            cvvEditText.setError(getString(R.string.valid_cvv));
            cvvEditText.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(firstNameEditText.getText().toString()))
        {
            scrollViewDown();
            firstNameEditText.setError(getString(R.string.valid_name));
            firstNameEditText.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(lastNameEditText.getText().toString()))
        {
            scrollViewDown();
            lastNameEditText.setError(getString(R.string.valid_name));
            lastNameEditText.requestFocus();
            return false;
        }
        return true;
    }

    private void scrollViewDown()
    {
        View lastChild = mainScrollView.getChildAt(mainScrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + mainScrollView.getPaddingBottom();
        int sy = mainScrollView.getScrollY();
        int sh = mainScrollView.getHeight();
        int delta = bottom - (sy + sh);

        mainScrollView.smoothScrollBy(0, delta);
    }

    private void cardBin(String iin)
    {
        TreepayAPI treepayAPI = ApiClient.getClient().create(TreepayAPI.class);

        CardBinRequest cardBinRequest = new CardBinRequest();
        cardBinRequest.setCard_bin_no(iin);
        cardBinRequest.setTp_langFlag(LocaleLanguage.getLanguage());
        treepayAPI.cardBin(cardBinRequest)
                .enqueue(new Callback<CardBinModel>()
                {
                    @Override
                    public void onResponse(Call<CardBinModel> call, Response<CardBinModel> response)
                    {
                        CardBinModel model = response.body();
                        if (model != null)
                        {
                            if("0000".equals(model.getRes_cd()))
                            {
                                if(!model.getCard_Type().isEmpty())
                                {
                                    if(model.getCard_Type().equals("CMCF"))
                                    {
                                        cardCompanyImageView.setImageResource(R.drawable.card_master);
                                    }
                                    else if(model.getCard_Type().equals("CVSF"))
                                    {
                                        cardCompanyImageView.setImageResource(R.drawable.card_visa);
                                    }
                                    else if(model.getCard_Type().equals("CJCF"))
                                    {
                                        cardCompanyImageView.setImageResource(R.drawable.card_jcb);
                                    }
                                }
                            }
                            else
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        TreepayCreditCardActivity.this);
                                alertDialogBuilder
                                        .setTitle(R.string.error_title)
                                        .setMessage("[" + model.getRes_cd() +"]"+ "\n" + model.getRes_msg())
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
                        else
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    TreepayCreditCardActivity.this);
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
                    public void onFailure(Call<CardBinModel> call, Throwable t)
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                TreepayCreditCardActivity.this);
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


    private void getOtt()
    {
        progressWheelDialog.show();
        TreepayAPI treepayAPI = ApiClient.getClient().create(TreepayAPI.class);

        GetOttRequest getOttRequest = new GetOttRequest();
        getOttRequest.setSite_cd(SiteInfo.getInstance().getSiteCd());
        getOttRequest.setExpiration_mm(expiration.substring(0, 2));
        getOttRequest.setExpiration_yy(expiration.substring(2, 4));
        getOttRequest.setCard_number(cardNumber);
        getOttRequest.setLast_card_holder_name(firstNameEditText.getText().toString());
        getOttRequest.setFirst_card_holder_name(lastNameEditText.getText().toString());
        getOttRequest.setVer(BuildConfig.TREEPAY_API_VERSION);
        getOttRequest.setTp_langFlag(LocaleLanguage.getLanguage());
        if(!saveCardCheckBox.isChecked())
        {
            treepayAPI.getCaOtt(getOttRequest)
                    .enqueue(new Callback<GetOttModel>()
                    {
                        @Override
                        public void onResponse(Call<GetOttModel> call, Response<GetOttModel> response)
                        {
                            progressWheelDialog.dismiss();
                            GetOttModel model = response.body();
                            if (model != null)
                            {
                                if("0000".equals(model.getRes_cd()))
                                {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra(CommonInfo.OTT, model.getOtt());
                                    returnIntent.putExtra(CommonInfo.CVV,cvvEditText.getText().toString());
                                    returnIntent.putExtra(CommonInfo.SAVE_CARD, saveCardCheckBox.isChecked());
                                    setResult(RESULT_OK,returnIntent);
                                    finish();
                                }
                                else
                                {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            TreepayCreditCardActivity.this);
                                    alertDialogBuilder
                                            .setTitle(R.string.error_title)
                                            .setMessage("[" + model.getRes_cd() +"]"+ "\n" + model.getRes_msg())
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
                            else
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        TreepayCreditCardActivity.this);
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
                        public void onFailure(Call<GetOttModel> call, Throwable t)
                        {
                            progressWheelDialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    TreepayCreditCardActivity.this);
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
        else
        {
            treepayAPI.getOcOtt(getOttRequest)
                    .enqueue(new Callback<GetOttModel>()
                    {
                        @Override
                        public void onResponse(Call<GetOttModel> call, Response<GetOttModel> response)
                        {
                            progressWheelDialog.dismiss();
                            GetOttModel model = response.body();
                            if (model != null)
                            {
                                if("0000".equals(model.getRes_cd()))
                                {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra(CommonInfo.OTT, model.getOtt());
                                    returnIntent.putExtra(CommonInfo.CVV,cvvEditText.getText().toString());
                                    returnIntent.putExtra(CommonInfo.SAVE_CARD, saveCardCheckBox.isChecked());
                                    setResult(RESULT_OK,returnIntent);
                                    finish();
                                }
                                else
                                {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            TreepayCreditCardActivity.this);
                                    alertDialogBuilder
                                            .setTitle(R.string.error_title)
                                            .setMessage("[" + model.getRes_cd() +"]"+ "\n" + model.getRes_msg())
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
                            else
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        TreepayCreditCardActivity.this);
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
                        public void onFailure(Call<GetOttModel> call, Throwable t)
                        {
                            progressWheelDialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    TreepayCreditCardActivity.this);
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
    }
}
