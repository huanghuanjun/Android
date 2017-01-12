package dhu.cst.zjm.encrypt.stores;


import dhu.cst.zjm.encrypt.action.Action;
import dhu.cst.zjm.encrypt.dispatcher.Dispatcher;

/**
 * Created by zjm on 2016/11/3.
 */

public abstract class Store {
    final Dispatcher dispatcher;

    protected Store(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    void emitStoreChange(String changePoint) {
        dispatcher.emitChange(changeEvent(changePoint));
    }

    abstract StoreChangeEvent changeEvent(String changePoint);

    public abstract void onAction(Action action);

    public interface StoreChangeEvent {
    }
}
