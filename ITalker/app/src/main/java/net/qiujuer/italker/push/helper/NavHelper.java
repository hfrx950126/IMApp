package net.qiujuer.italker.push.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * 解决对Fragment的调度与重用问题
 * Created by Administrator on 2017/7/25 0025.
 */
public class NavHelper<T> {
    //所有的Tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();

    //用于初始化的必须参数
    private final Context context;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangeListener<T> listener;
    //当前一个选中的Tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId,
                     FragmentManager fragmentManager,
                     OnTabChangeListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    /**
     * 添加Tab
     * @param menuId  Tab对应的菜单Id
     * @param tab Tab
     */
    public NavHelper<T> add(int menuId,Tab<T> tab){
        tabs.put(menuId,tab);
        return this;
    }

    public Tab<T> getCurrentTab(){
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     * @param menuId 菜单的ID
     * @return 是否能够处理这个点击
     */
    public boolean performClickMenu(int menuId){
        //集合中寻找点击的菜单对应的Tab
        //如果有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if(tab!=null){
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真是的Tab选择操作
     * @param tab tab
     */
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;

        if(currentTab!=null){
            oldTab = currentTab;
            if(oldTab==tab){
                //如果当前的Tab就是点击的Tab，
                //那么我们不做处理。
                notifyTabReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChange(currentTab,oldTab);
    }

    /**
     * 进行Fragment的真实的调度操作
     * @param newTab 新的
     * @param oldTab 旧的
     */
    private void doTabChange(Tab<T> newTab,Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(oldTab!=null){
            if(oldTab.fragment!=null){
                //从界面移除，但是还在Fragment的缓存空间中
                ft.detach(oldTab.fragment);
            }
        }
        if(newTab!=null){
            if(newTab.fragment==null){
                //首次新建
                Fragment fragment = Fragment.instantiate(context,newTab.clx.getName(),null);
                //缓存起来
                newTab.fragment = fragment;
                ft.add(containerId,fragment,newTab.clx.getName());
            }else{
                ft.attach(newTab.fragment);
            }
        }
        //提交事务
        ft.commit();
        notifyTabSelect(newTab,oldTab);
    }

    /**
     * 回调我们的监听器
     * @param newTab  新的Tab
     * @param oldTab   旧的Tab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab){
        if(listener!=null){
            listener.onTabChanged(newTab,oldTab);
        }
    }
    private void notifyTabReselect(Tab<T> tab){
        //TODO 二次点击Tab所做的操作
    }
    /**
     * 所有Tab的基础属性
     * @param <T> 泛型的额外参数
     */

    public static class Tab<T>{
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        //Fragment对应的Class信息
        public Class<?> clx;
        //额外的字段，用户自己设定需要调用
        public T extra;
        //内部缓存的定义的Fragment
        Fragment fragment;
    }

    /**
     * 定义事件处理完成后的回掉接口
     * @param <T>
     */
    public interface OnTabChangeListener<T>{
        void onTabChanged(Tab<T> newTab,Tab<T> oldTab);
    }
}
