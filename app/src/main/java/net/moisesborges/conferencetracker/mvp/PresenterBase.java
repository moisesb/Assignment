package net.moisesborges.conferencetracker.mvp;

/**
 * Created by Moisés on 18/08/2016.
 */

public interface PresenterBase<T extends ViewBase> {
    void bindView(T view);

    void unbindView();
}
