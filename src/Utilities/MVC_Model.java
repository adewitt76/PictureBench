package Utilities;

import java.util.ArrayList;

/**
 * Created by aaron on 5/9/15.
 */
public interface MVC_Model {

    ArrayList<MVC_Listener> listenerList = new ArrayList<>();

    default void registerListener(MVC_Listener listener){
        listenerList.add(listener);
    }

    default void removeListener(MVC_Listener listener){
        listenerList.remove(listener);
    }

    default void notifyListeners() {
        for(MVC_Listener listener: listenerList){
            listener.listenerEvent(ListenerEvent.MODEL_CHANGE);
        }
    }
}
