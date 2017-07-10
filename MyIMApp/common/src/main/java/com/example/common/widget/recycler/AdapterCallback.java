package com.example.common.widget.recycler;

import android.provider.ContactsContract;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public interface AdapterCallback<Data> {
   void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
