package com.kktt.jesus.schedule.task;

import com.kktt.jesus.dataobject.AliExpressItem;
import lombok.Data;

import java.util.List;

@Data
public class AliExpressListTask extends FeedBaseTask{
    public interface TYPE{
        byte PRICE = 10;
        byte INVENTORY = 20;
        byte DELETE = 30;
    }

    List<AliExpressItem> tasks;

    private byte type;
}
