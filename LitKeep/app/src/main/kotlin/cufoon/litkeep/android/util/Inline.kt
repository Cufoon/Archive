package cufoon.litkeep.android.util

inline fun <T, R> T?.ifNotNullOrElse(ifNotNullPath: (T) -> R, ifNull: () -> R) =
    also { if (it == null) ifNull() else ifNotNullPath(it) }
