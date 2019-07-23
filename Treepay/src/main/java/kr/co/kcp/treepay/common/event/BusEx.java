package kr.co.kcp.treepay.common.event;

import com.squareup.otto.Bus;

import java.util.ArrayList;

/**
 * Created by hanh on 2016-07-27.
 */
public class BusEx extends Bus
{
    private ArrayList registeredObjects = new ArrayList<>();

    @Override
    public void register(Object object)
    {
        if (!registeredObjects.contains(object))
        {
            registeredObjects.add(object);
            super.register(object);
        }
    }

    @Override
    public void unregister(Object object)
    {
        if (registeredObjects.contains(object))
        {
            registeredObjects.remove(object);
            super.unregister(object);
        }
    }
}
