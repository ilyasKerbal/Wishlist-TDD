package com.raywenderlich.android.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.raywenderlich.android.wishlist.persistance.RepositoryImpl
import com.raywenderlich.android.wishlist.persistance.WishlistDao
import com.raywenderlich.android.wishlist.persistance.WishlistDaoImpl
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DetailsViewModelTests {

    @get : Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val wishlistDao : WishlistDao = Mockito.spy(WishlistDaoImpl())
    private val detailViewModel  = DetailViewModel(RepositoryImpl(wishlistDao))

    private val wishlist = Wishlist("Father", listOf("Laptop", "iPhone"), 1)

    private val name = "Electronics"
    @Test
    fun saveNewItem_shouldCallDatabase() {
        detailViewModel.saveNewItem(wishlist, name)

        verify(wishlistDao).save(eq(wishlist.copy(wishes = wishlist.wishes + name)))
    }

    @Test
    fun saveNewItem_savesData() {
        detailViewModel.saveNewItem(wishlist, name)

        val mockObserver = mock<Observer<Wishlist>>()

        wishlistDao.findById(wishlist.id).observeForever(mockObserver)

        verify(mockObserver).onChanged(wishlist.copy(wishes = wishlist.wishes + name))
    }

    @Test
    fun getWishlist_shouldCallDatabase() {
        detailViewModel.getWishlist(1)
        verify(wishlistDao).findById(any())
    }

    @Test
    fun getWishlist_returnsCorrectData() {
        wishlistDao.save(wishlist)
        val mockObserver = mock<Observer<Wishlist>>()
        detailViewModel.getWishlist(1).observeForever(mockObserver)

        verify(mockObserver).onChanged(eq(wishlist))
    }
}