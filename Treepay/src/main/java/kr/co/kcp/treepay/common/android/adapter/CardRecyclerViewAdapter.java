package kr.co.kcp.treepay.common.android.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import kr.co.kcp.treepay.common.android.adapter.Delegate.CardItemDelegate;
import kr.co.kcp.treepay.common.model.CardListModel;


/**
 * Created by hanh on 2016-07-20.
 */
public class CardRecyclerViewAdapter extends MultiItemTypeAdapter<CardListModel.card_list>
{
    public CardRecyclerViewAdapter(Context context, List<CardListModel.card_list> datas)
    {
        super(context, datas);
        addItemViewDelegate(new CardItemDelegate(context));
    }
}
