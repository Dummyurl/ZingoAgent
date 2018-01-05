package app.zingo.com.zingoagent.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ZingoHotels.com on 10-11-2017.
 */

public class PreferenceHandler {

    private SharedPreferences sh;
    private static PreferenceHandler preferanceHandlerInstance = null;

    private PreferenceHandler() {

    }

    private PreferenceHandler(Context mContext) {
        sh = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public static synchronized PreferenceHandler getInstance(Context mContext) {
        if (null == preferanceHandlerInstance)
        {
            preferanceHandlerInstance = new PreferenceHandler(mContext);
        }
        return preferanceHandlerInstance;
    }





    public void setUserId(int id)
    {
        sh.edit().putInt(Constants.USER_ID,id).apply();
    }

    public int getUserId()
    {
        return sh.getInt(Constants.USER_ID,0);
    }

    public void setHotelId(int id)
    {
        sh.edit().putInt(Constants.HOTEL_ID,id).apply();
    }

    public int getHotelID()
    {
        return sh.getInt(Constants.HOTEL_ID,0);
    }

    public void clear(){
        sh.edit().clear().apply();

    }

}
