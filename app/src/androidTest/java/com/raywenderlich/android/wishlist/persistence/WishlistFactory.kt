package com.raywenderlich.android.wishlist.persistence

import com.raywenderlich.android.wishlist.Wishlist
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object WishlistFactory {

    private fun makeRandomUUID() : String = UUID.randomUUID().toString()
    private fun makeRandomInt() = ThreadLocalRandom.current().nextInt(0, 1001)

    fun makeWishlist() : Wishlist = Wishlist(makeRandomUUID(), listOf(makeRandomUUID(), makeRandomUUID()), makeRandomInt())
}