package net.frozendevelopment.frozenhue.extensions

import io.realm.RealmList

fun <T> RealmList<T>.fromList(list: List<T>): RealmList<T> {
    this.addAll(list)
    return this
}
