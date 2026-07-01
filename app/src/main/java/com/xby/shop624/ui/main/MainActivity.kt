package com.xby.shop624.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import com.xby.shop624.R
import com.xby.shop624.databinding.ActivityMainBinding
import com.xby.shop624.ui.cart.CartFragment
import com.xby.shop624.ui.cart.CartViewModel
import com.xby.shop624.ui.category.CategoryFragment
import com.xby.shop624.ui.home.HomeFragment
import com.xby.shop624.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val cartViewModel: CartViewModel by viewModels()

    private lateinit var homeFragment: HomeFragment
    private lateinit var categoryFragment: CategoryFragment
    private lateinit var cartFragment: CartFragment
    private lateinit var profileFragment: ProfileFragment

    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            categoryFragment = CategoryFragment()
            cartFragment = CartFragment()
            profileFragment = ProfileFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, profileFragment, "profile").hide(profileFragment)
                .add(R.id.fragment_container, cartFragment, "cart").hide(cartFragment)
                .add(R.id.fragment_container, categoryFragment, "category").hide(categoryFragment)
                .add(R.id.fragment_container, homeFragment, "home")
                .commit()
            activeFragment = homeFragment
        } else {
            homeFragment = supportFragmentManager.findFragmentByTag("home") as HomeFragment
            categoryFragment = supportFragmentManager.findFragmentByTag("category") as CategoryFragment
            cartFragment = supportFragmentManager.findFragmentByTag("cart") as CartFragment
            profileFragment = supportFragmentManager.findFragmentByTag("profile") as ProfileFragment
            activeFragment = homeFragment
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val target = when (item.itemId) {
                R.id.nav_home -> homeFragment
                R.id.nav_category -> categoryFragment
                R.id.nav_cart -> cartFragment
                R.id.nav_profile -> profileFragment
                else -> return@setOnItemSelectedListener false
            }
            switchFragment(target)
            true
        }

        cartViewModel.cartCount.observe(this) { count ->
            updateCartBadge(count)
        }

        handleTabIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleTabIntent(intent)
    }

    private fun handleTabIntent(intent: Intent?) {
        if (intent?.getStringExtra(EXTRA_TAB) == TAB_CART) {
            binding.bottomNavigation.selectedItemId = R.id.nav_cart
            switchFragment(cartFragment)
            intent.removeExtra(EXTRA_TAB)
        }
    }

    private fun updateCartBadge(count: Int) {
        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.nav_cart)
        badge.isVisible = count > 0
        if (count > 0) {
            badge.number = count
            badge.badgeGravity = BadgeDrawable.TOP_END
        } else {
            binding.bottomNavigation.removeBadge(R.id.nav_cart)
        }
    }

    private fun switchFragment(target: Fragment) {
        if (target == activeFragment) return
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(target)
            .commit()
        activeFragment = target
    }

    companion object {
        const val EXTRA_TAB = "extra_tab"
        const val TAB_CART = "cart"
    }
}
