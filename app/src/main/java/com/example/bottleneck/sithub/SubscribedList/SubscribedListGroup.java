package com.example.bottleneck.sithub.SubscribedList;

import java.util.ArrayList;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class SubscribedListGroup {

    private String mName;
    private ArrayList<Listchild> mItems;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public ArrayList<Listchild> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Listchild> items) {
        this.mItems = items;
    }

}
