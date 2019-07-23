package kr.co.kcp.treepay.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import kr.co.kcp.treepay.common.R;

public class ProgressWheelDialog extends Dialog
{
    private Context context;

    public ProgressWheelDialog(Context context)
    {
        super(context, R.style.ProgressWheelDialog);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress_wheel);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
    }

    @Override
    public void show()
    {
        if(context != null && context instanceof Activity)
        {
            if (!((Activity)context).isFinishing())
            {
                super.show();
            }
        }
    }
}
