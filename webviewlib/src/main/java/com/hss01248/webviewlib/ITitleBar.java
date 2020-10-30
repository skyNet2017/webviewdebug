package com.hss01248.webviewlib;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;
import com.wuhenzhizao.titlebar.utils.ScreenUtils;

public interface ITitleBar {
    /**
     * 设置背景颜色
     *
     * @param color
     */
    
    default void setBackgroundColor(int color) {

    }

    /**
     * 设置背景图片
     *
     * @param resource
     */
    
    default void setBackgroundResource(int resource) {

    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    default void setStatusBarColor(int color) {

    }

    /**
     * 是否填充状态栏
     *
     * @param show
     */
    default void showStatusBar(boolean show) {

    }

    /**
     * 切换状态栏模式
     */
    default void toggleStatusBarMode() {

    }

    /**
     * 获取标题栏底部横线
     *
     * @return
     */
    default View getButtomLine() {
        return null;
    }

    /**
     * 获取标题栏左边TextView，对应leftType = textView
     *
     * @return
     */
    default TextView getLeftTextView() {
        return null;
    }

    /**
     * 获取标题栏左边ImageButton，对应leftType = imageButton
     *
     * @return
     */
    default ImageButton getLeftImageButton() {
        return null;
    }

    /**
     * 获取标题栏右边TextView，对应rightType = textView
     *
     * @return
     */
    default TextView getRightTextView() {
        return null;
    }

    /**
     * 获取标题栏右边ImageButton，对应rightType = imageButton
     *
     * @return
     */
    default ImageButton getRightImageButton() {
        return null;
    }

    default LinearLayout getCenterLayout() {
        return null;
    }

    /**
     * 获取标题栏中间TextView，对应centerType = textView
     *
     * @return
     */
    default TextView getCenterTextView() {
        return null;
    }

    default TextView getCenterSubTextView() {
        return null;
    }

    /**
     * 获取搜索框布局，对应centerType = searchView
     *
     * @return
     */
    default RelativeLayout getCenterSearchView() {
        return null;
    }

    /**
     * 获取搜索框内部输入框，对应centerType = searchView
     *
     * @return
     */
    default EditText getCenterSearchEditText() {
        return null;
    }

    /**
     * 获取搜索框右边图标ImageView，对应centerType = searchView
     *
     * @return
     */
    default ImageView getCenterSearchRightImageView() {
        return null;
    }

    default ImageView getCenterSearchLeftImageView() {
        return null;
    }

    /**
     * 获取左边自定义布局
     *
     * @return
     */
    default View getLeftCustomView() {
        return null;
    }

    /**
     * 获取右边自定义布局
     *
     * @return
     */
    default View getRightCustomView() {
        return null;
    }

    /**
     * 获取中间自定义布局视图
     *
     * @return
     */
    default View getCenterCustomView() {
        return null;
    }

    /**
     * @param leftView
     */
    default void setLeftView(View leftView) {

    }

    /**
     * @param centerView
     */
    default void setCenterView(View centerView) {

    }

    /**
     * @param rightView
     */
    default void setRightView(View rightView) {

    }

    /**
     * 显示中间进度条
     */
    default void showCenterProgress() {

    }

    /**
     * 隐藏中间进度条
     */
    default void dismissCenterProgress() {

    }

    /**
     * 显示或隐藏输入法,centerType="searchView"模式下有效
     *
     * @return
     */
    default void showSoftInputKeyboard(boolean show) {

    }

    /**
     * 设置搜索框右边图标
     *
     * @param res
     */
    default void setSearchRightImageResource(int res) {

    }

    /**
     * 获取SearchView输入结果
     */
    default String getSearchKey() {

        return "";
    }

    /**
     * 设置点击事件监听
     *
     * @param listener
     */

    default void setListener(OnTitleBarListener listener) {

    }

    default void setDoubleClickListener(OnTitleBarDoubleClickListener doubleClickListener) {

    }

    default void setOnLongClickListener(View.OnLongClickListener listener){

    }

    /**
     * 点击事件
     */
    public interface OnTitleBarListener {
        /**
         * @param v
         * @param action 对应ACTION_XXX, 如ACTION_LEFT_TEXT
         * @param extra  中间为搜索框时,如果可输入,点击键盘的搜索按钮,会返回输入关键词
         */
        void onClicked(View v, int action, String extra);
    }

    /**
     * 标题栏双击事件监听
     */
    public interface OnTitleBarDoubleClickListener {
        void onClicked(View v);
    }
}
