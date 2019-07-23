package kr.co.kcp.treepay.common.android.adapter.Delegate;


import android.content.Context;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import kr.co.kcp.treepay.common.R;
import kr.co.kcp.treepay.common.model.CardListModel;

/**
 * Created by hanh on 2016-07-20.
 */
public class CardItemDelegate implements ItemViewDelegate<CardListModel.card_list>
{
    private Context context;

    public CardItemDelegate(Context context)
    {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.item_credit_card;
    }

    @Override
    public boolean isForViewType(CardListModel.card_list item, int position)
    {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, CardListModel.card_list cardlist, int position)
    {
        holder.setText(R.id.cardNumbreTextView, "****-****-****-" + cardlist.getCard_last_num());
        if(cardlist.getCard_brand().equals("CMCF"))
        {
            holder.setImageResource(R.id.cardCompanyImageView, R.drawable.card_master);
        }
        else if(cardlist.getCard_brand().equals("CVSF"))
        {
            holder.setImageResource(R.id.cardCompanyImageView, R.drawable.card_visa);
        }
        else if(cardlist.getCard_brand().equals("CJCF"))
        {
            holder.setImageResource(R.id.cardCompanyImageView, R.drawable.card_jcb);
        }

        if(cardlist.isCheck())
        {
            holder.getView(R.id.creditCardItemRelativeLayout).setSelected(true);
        }
        else
        {
            holder.getView(R.id.creditCardItemRelativeLayout).setSelected(false);
        }
    }
}