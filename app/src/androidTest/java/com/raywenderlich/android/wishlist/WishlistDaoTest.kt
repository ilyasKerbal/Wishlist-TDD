package com.raywenderlich.android.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raywenderlich.android.wishlist.persistance.WishlistDao
import com.raywenderlich.android.wishlist.persistance.WishlistDatabase
import com.raywenderlich.android.wishlist.persistence.WishlistFactory
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class WishlistDaoTest {

    @get: Rule
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var wishlistDatabase: WishlistDatabase
    private lateinit var wishlistDao: WishlistDao
    private lateinit var wishlist1 : Wishlist
    private lateinit var wishlist2 : Wishlist

    @Before
    fun initDb() {
        wishlistDatabase =
            Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), WishlistDatabase::class.java)
                .build()

        wishlistDao = wishlistDatabase.wishlistDao()

        wishlist1 = WishlistFactory.makeWishlist()
        wishlist2 = WishlistFactory.makeWishlist()
    }

    @After
    fun cleanup() {
        wishlistDatabase.close()
    }

    @Test
    fun getAll_returnsEmptyList() {
        val testObserver = mock<Observer<List<Wishlist>>>()

        wishlistDao.getAll().observeForever(testObserver)

        verify(testObserver).onChanged(emptyList())
    }

    @Test
    fun saveWishlists_savesData() {
        wishlistDao.save(wishlist1, wishlist2)

        val testObserver: Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<Wishlist>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        assertThat(argumentCaptor.value.size, greaterThan(0))
    }

    @Test
    fun getAll_shouldRetrieveData() {
        wishlistDao.save(wishlist1, wishlist2)

        val testObserver: Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<Wishlist>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        val captureArgument = argumentCaptor.value

        assertThat(captureArgument, containsInAnyOrder(wishlist1, wishlist2))
        assertThat(captureArgument.size, equalTo(2))
    }

    @Test
    fun findById_shouldRetrieveCorrectData() {
        wishlistDao.save(wishlist1, wishlist2)

        val testObserver: Observer<Wishlist> = mock()

        wishlistDao.findById(wishlist2.id).observeForever(testObserver)

        verify(testObserver).onChanged(wishlist2)
    }
}