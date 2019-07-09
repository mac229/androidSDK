package eu.espeo.ipgcashierlib

import androidx.fragment.app.Fragment


inline fun <reified T> Fragment.getListenerOrThrowException(): T {
    return getListener<T>()
        ?: throw IllegalStateException("Calling class must implement:  " + T::class.java.simpleName)
}

inline fun <reified T> Fragment.getListener(): T? {
    var listener = getListenerFromTargetFragment<T>()
    if (listener != null) {
        return listener
    }

    listener = getListenerFromParentFragment<T>()
    if (listener != null) {
        return listener
    }

    return getListenerFromActivity()
}

inline fun <reified T> getListener(target: Any?): T? {
    return if (T::class.java.isInstance(target)) {
        T::class.java.cast(target)
    } else {
        null
    }
}

inline fun <reified T> Fragment.getListenerFromTargetFragment(): T? {
    return getListener(targetFragment)
}

inline fun <reified T> Fragment.getListenerFromParentFragment(): T? {
    return getListener<T>(parentFragment)
}

inline fun <reified T> Fragment.getListenerFromActivity(): T? {
    return getListener<T>(activity)
}