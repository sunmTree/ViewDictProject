package com.sunm.widght.htextview;

import android.util.Log;

import com.sunm.globle.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 字符处理工具类
 * Created by sunmeng on 2016/12/19.
 */

public class CharacterUtils {

    private static boolean LOG_DEBUG = Config.DEBUG;
    private static final String TAG = "CharacterUtils";

    /****
     * 对比新旧字符串，返回需要保留的字符，以及移动的位置
     *
     * @param oldText 原来的字符串
     * @param text    新出现的字符串
     * @return 保留的字符，以及移动的位置
     */
    public static List<CharacterDiffResult> diff(CharSequence oldText, CharSequence text) {

        List<CharacterDiffResult> differentList = new ArrayList<>();
        Set<Integer> skip = new HashSet<>();

        if (LOG_DEBUG) {
            Log.i(TAG, " CharacterUtils diff with oldText: " + oldText + " and newText: " + text);
        }

        for (int i = 0; i < oldText.length(); i++) {
            char c = oldText.charAt(i);
            for (int j = 0; j < text.length(); j++) {
                if (!skip.contains(j) && c == text.charAt(j)) {
                    skip.add(j);
                    CharacterDiffResult different = new CharacterDiffResult();
                    different.c = c;
                    different.fromIndex = i;
                    different.moveIndex = j;
                    differentList.add(different);
                    break;
                }
            }
        }

        return differentList;
    }


    public static int needMove(int index, List<CharacterDiffResult> diffResultList) {
        for (CharacterDiffResult different : diffResultList) {
            if (different.fromIndex == index) {
                return different.moveIndex;
            }
        }
        return -1;
    }


    public static boolean stayHere(int index, List<CharacterDiffResult> diffResultList) {
        for (CharacterDiffResult diffResult : diffResultList) {
            if (diffResult.moveIndex == index) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回从原来的字符串的from下标移动到新的字符串move下标在进度为progress的x坐标
     * @param from 原来的字符串的from下标
     * @param move 新的字符串move下标
     * @param progress 移动的进度 0~1
     * @param startX 新字符串位移初始值
     * @param oldStartX 原来字符串位移初始值
     * @param gaps 原来字符串每个字符的间距
     * @param oldGaps 新字符串每个字符的间距
     * @return
     */
    public static float getOffset(int from, int move, float progress, float startX, float oldStartX, float gaps[], float oldGaps[]){
        // 计算目标点
        float dist = startX;
        for (int i = 0 ; i < move ; i++){
            dist += gaps[i];
        }

        // 计算当前点
        float cur = oldStartX;
        for (int i=0; i< from; i++){
            cur += oldGaps[i];
        }

        return cur + (dist - cur) * progress;
    }
}
