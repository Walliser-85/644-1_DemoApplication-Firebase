package ch.hevs.aislab.demo.util;

/**
 * This generic interface is used as custom callback for async tasks.
 * For an example usage see {@link ch.hevs.aislab.demo.ui.mgmt.RegisterActivity:75}.
 *
 * @param <T> Value shared with the class issuing the async task.
 */
public interface OnAsyncEventListener<T> {
    void onSuccess(T object);
    void onFailure(Exception e);
}
