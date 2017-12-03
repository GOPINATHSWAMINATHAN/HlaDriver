package com.hlacab.hladriver.model;

/**
 * Created by gopinath on 03/12/17.
 */

public class Result {

    public String message_id;

    Result()
    {

    }


    public Result(String message_id)
    {
        this.message_id=message_id;
    }
    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
