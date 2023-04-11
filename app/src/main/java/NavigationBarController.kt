import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ru.piece.of.crown.sveter.com.R

class NavigationBarController (
    context: Context,
    private val containerViewId: Int
) {
    private val itemsViews = mutableListOf<List<View>>()
    private val itemFragments = mutableListOf<Fragment>()
    private val fragmentActivity = context as FragmentActivity
    
    private var onSelectListener: ((views: List<View>, index: Int) -> Unit)? = null
    private var onUnselectListener: ((views: List<View>, index: Int) -> Unit)? = null
    
    private var currentItemIndex = 0

    fun init() {
        currentItemIndex = 0

        fragmentActivity.supportFragmentManager
            .beginTransaction()
            .add(containerViewId, itemFragments[0])
            .commit()

        onSelectListener?.let { it(itemsViews[currentItemIndex], currentItemIndex) }
    }

    fun addItem(fragment: Fragment, vararg views: View) {
        itemFragments.add(fragment)
        itemsViews.add(listOf(*views))
    }

    fun selectItem(itemIndex: Int) {
        if (itemIndex == currentItemIndex) return

        onUnselectListener?.let { it(itemsViews[currentItemIndex], currentItemIndex) }

        if (itemIndex > currentItemIndex)
            fragmentActivity.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right)
                .replace(containerViewId, itemFragments[itemIndex])
                .commit()
        else
            fragmentActivity.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(containerViewId, itemFragments[itemIndex])
                .commit()

        onSelectListener?.let { it(itemsViews[itemIndex], itemIndex) }

        currentItemIndex = itemIndex
    }

    fun setOnSelectListener(onSelectListener: (views: List<View>, index: Int) -> Unit) {
        this.onSelectListener = onSelectListener
    }

    fun setOnUnselectListener(onUnselectListener: (views: List<View>, index: Int) -> Unit) {
        this.onUnselectListener = onUnselectListener
    }
}